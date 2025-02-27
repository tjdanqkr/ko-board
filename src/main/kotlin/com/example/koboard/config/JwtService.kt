package com.example.koboard.config

import com.example.koboard.dto.TokenDto


interface JwtService {
    fun createAccessToken(name: String): TokenDto
    fun jwtDecoder(jwt: String): String
    fun createRefreshToken(jwt: String): TokenDto
    fun jwtRefreshDecoder(jwt: String): String
}