package com.example.koboard.dto

import com.example.koboard.domain.User
import java.time.LocalDateTime

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromUser(user: User) = UserResponse(
            id = user.id!!,
            name = user.name,
            email = user.email,
            createdAt = user.createdAt
        )
    }
}