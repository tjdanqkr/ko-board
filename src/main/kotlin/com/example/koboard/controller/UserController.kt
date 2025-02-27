package com.example.koboard.controller

import com.example.koboard.dto.UserResponse
import com.example.koboard.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {
    @GetMapping("/api/v1/users")
    fun getAllUsers(): List<UserResponse> {
        return userService.getAllUsers()
    }


}