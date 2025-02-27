package com.example.koboard.dto

import java.time.LocalDateTime

data class TokenResponse (
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: LocalDateTime,
    val refreshToken: String,
)