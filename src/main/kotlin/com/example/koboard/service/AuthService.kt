package com.example.koboard.service

import com.example.koboard.dto.SignInRequest
import com.example.koboard.dto.TokenResponse
import com.example.koboard.dto.UserRequest

interface AuthService {
    fun signIn(request: SignInRequest): TokenResponse
    fun signUp(userRequest: UserRequest)
    fun refresh(accessToken: String, refreshToken: String): TokenResponse
}