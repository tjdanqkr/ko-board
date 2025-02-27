package com.example.koboard.service


import com.example.koboard.dto.UserRequest
import com.example.koboard.dto.UserResponse

interface UserService {
    fun getUserById(id: Int): UserResponse
    fun createUser(request: UserRequest): UserResponse
    fun updateUser(request: UserRequest, id: Int): UserResponse
    fun deleteUser(id: Int)
    fun getAllUsers(): List<UserResponse>
}