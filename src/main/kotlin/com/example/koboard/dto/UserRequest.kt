package com.example.koboard.dto

import com.example.koboard.domain.User

data class UserRequest(
    val name: String? = null ,
    val email: String? = null ,
    val password: String? = null ,
)
fun UserRequest.toEntity(encodedPassword: String):User{
    when {
        name.isNullOrBlank() -> throw IllegalArgumentException("Name must not be blank")
        email.isNullOrBlank() -> throw IllegalArgumentException("Email must not be blank")
        password.isNullOrBlank() -> throw IllegalArgumentException("Password must not be blank")
        else -> return User(
            id = null,
            name = name,
            email = email,
            _password = encodedPassword
        )
    }
}
