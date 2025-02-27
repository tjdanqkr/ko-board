package com.example.koboard.repository

import com.example.koboard.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int> {
    fun findByName(name: String): User?
    fun findByEmail(email: String): User?
}