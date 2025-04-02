package com.example.koboard.controller

import com.example.koboard.domain.User
import com.example.koboard.dto.SignInRequest
import com.example.koboard.dto.UserRequest
import com.example.koboard.dto.UserResponse
import com.example.koboard.service.AuthService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class AuthController (
    private val authService: AuthService
){
    @PostMapping("/api/v1/auth/sign-up")
    fun createUser(@RequestBody request: UserRequest) = authService.signUp(request)


    @PostMapping("/api/v1/auth/sign-in")
    fun signIn(@RequestBody request: SignInRequest) = authService.signIn(request)


    @GetMapping("/api/v1/auth/me")
    fun getMe(@AuthenticationPrincipal me: User) = UserResponse.fromUser(me)

    @PostMapping("/api/v1/auth/refresh")
    fun refresh(
        @RequestHeader("Authorization") bearerToken: String,
        @RequestHeader("Refresh-Token") refreshToken: String
    ) = authService.refresh(bearerToken.substringAfter("Bearer "), refreshToken)

}