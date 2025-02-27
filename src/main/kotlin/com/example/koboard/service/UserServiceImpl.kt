package com.example.koboard.service

import com.example.koboard.config.JwtService
import com.example.koboard.dto.*
import com.example.koboard.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) : UserService, UserDetailsService, AuthService{

    override fun getUserById(id: Int): UserResponse = userRepository.findByIdOrNull(id)
        ?.let { UserResponse.fromUser(it) }
        ?: throw IllegalArgumentException("User not found")
    override fun createUser(request: UserRequest): UserResponse = userRepository.save(request.toEntity(passwordEncoder.encode(request.password))).let { UserResponse.fromUser(it) }
    @Transactional
    override fun updateUser(request: UserRequest, id: Int): UserResponse =
        userRepository.findByIdOrNull(id)
            ?.apply { update(request) }
            ?.let { UserResponse.fromUser(it) }
            ?: throw IllegalArgumentException("User not found")

    override fun deleteUser(id: Int) = userRepository.deleteById(id)
    override fun getAllUsers(): List<UserResponse> = userRepository.findAll().map { UserResponse.fromUser(it) }
    override fun loadUserByUsername(username: String?): UserDetails = username?.let { userRepository.findByName(it) } ?: throw IllegalArgumentException("User not found")
    override fun signIn(request: SignInRequest): TokenResponse {
        val user = userRepository.findByEmail(request.email) ?: throw IllegalArgumentException("User not found")
        if (!passwordEncoder.matches(request.password, user.password)) throw IllegalArgumentException("Password is incorrect")
        val dto = jwtService.createAccessToken(user.name)
        val accessToken = dto.token
        val expiresIn = dto.expiresAt
        return TokenResponse(
            accessToken = accessToken,
            refreshToken = jwtService.createRefreshToken(accessToken).token,
            expiresIn = expiresIn
        )
    }

    override fun signUp(userRequest: UserRequest) {
        when {
            userRepository.findByEmail(userRequest.email?:throw IllegalArgumentException("User already exists")) != null -> throw IllegalArgumentException("User already exists")
            else -> userRepository.save(userRequest.toEntity(passwordEncoder.encode(userRequest.password?:throw IllegalArgumentException("Password must not be blank")))
            )
        }

    }

    override fun refresh(accessToken: String, refreshToken: String): TokenResponse {
        val refreshDecoder = jwtService.jwtRefreshDecoder(refreshToken)
        if (refreshDecoder != accessToken) throw IllegalArgumentException("Invalid refresh token")
        val username = jwtService.jwtDecoder(accessToken)
        val user = userRepository.findByName(username) ?: throw IllegalArgumentException("User not found")
        val dto = jwtService.createAccessToken(user.name)
        val access = dto.token
        val expiresAt = dto.expiresAt
        return TokenResponse(
            accessToken = access,
            refreshToken = jwtService.createRefreshToken(access).token,
            expiresIn = expiresAt
        )
    }
}