package com.example.koboard.service

import com.example.koboard.config.JwtServiceImpl
import com.example.koboard.domain.User
import com.example.koboard.dto.UserRequest
import com.example.koboard.repository.UserRepository
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@DataJpaTest
class UserServiceImplTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val em: EntityManager
){
    private lateinit var userService: UserService
    private lateinit var user: User
    @BeforeEach
    fun setUp() {
        val jwtService = JwtServiceImpl(
            "hi",
            "hi",
            1000,
            "hi",
            1000
        )
        userService = UserServiceImpl(userRepository, jwtService, BCryptPasswordEncoder() )
        user = User(
            name = "test",
            email = "test@test.com",
            _password = "test"
        )
        userRepository.save(user)
    }
    @Nested
    inner class GetUserById{
        @Test
        fun `should return user by id`(){
            val user = userService.getUserById(user.id!!)
            assertEquals("test", user.name)
        }
        @Test
        fun `should throw exception if user not found`(){
            assertThrows(IllegalArgumentException::class.java){
                userService.getUserById(0)
            }
        }
    }

    @Nested
    inner class CreateUser{
        @Test
        fun `should create user`() {
            // given
            val request = UserRequest(
                name = "test2",
                email = "test@test.com",
                password = "test"
            )
            // when
            val user = userService.createUser(request)
            // then
            assertEquals("test2", user.name)
        }
        @Test
        fun `should throw exception if name is null`() {
            // given
            val request = UserRequest(
                name = "",
                email = "test@test.com",
                password = "test"
            )
            // then
            val assertThrows = assertThrows(IllegalArgumentException::class.java) {
                userService.createUser(request)
            }
            assertEquals("Name must not be blank", assertThrows.message)
        }

    }
    @Nested
    inner class UpdateUser{
        @Test
        fun `should update user`() {
            // given
            val request = UserRequest(
                name = "test2",
            )
            // when
            val updatedUser = userService.updateUser(
                request, user.id!!
            )
            // then
            assertEquals("test2", updatedUser.name)
            assertEquals("test@test.com", updatedUser.email)
            em.flush()
//            em.clear()
            val message = userRepository.findById(user.id!!).get()

            assertTrue(message == user)
        }
        @Test
        fun `should throw exception if user not found`() {
            // given
            val request = UserRequest(
                name = "test2",
            )
            // then
            val assertThrows = assertThrows(IllegalArgumentException::class.java) {
                userService.updateUser(request, 0)
            }
            assertEquals("User not found", assertThrows.message)
        }
    }
    @Nested
    inner class DeleteUser{
        @Test
        fun `should delete user`() {
            // when
            userService.deleteUser(user.id!!)
            // then
            val assertThrows = assertThrows(IllegalArgumentException::class.java) {
                userService.getUserById(user.id!!)
            }
            assertEquals("User not found", assertThrows.message)
        }
    }
    @Nested
    inner class GetAllUsers{
        @Test
        fun `should return all users`() {
            // when
            val users = userService.getAllUsers()
            // then
            assertEquals(1, users.size)
        }
    }
}

