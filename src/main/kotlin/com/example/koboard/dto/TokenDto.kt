package com.example.koboard.dto

import java.time.LocalDateTime

data class TokenDto(
    val token: String,
    val expiresAt: LocalDateTime
)
