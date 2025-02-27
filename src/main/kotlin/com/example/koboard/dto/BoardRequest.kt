package com.example.koboard.dto

import com.example.koboard.domain.Board
import com.example.koboard.domain.User

data class BoardRequest(
    val name: String?,
    val description: String?,
)
fun BoardRequest.toEntity(user: User):Board {
    when {
        name.isNullOrBlank() ->
            throw IllegalArgumentException("Name must not be blank")
        description.isNullOrBlank() ->
            throw IllegalArgumentException("Description must not be blank")
        else -> return Board(
            name = name,
            description = description,
            owner = user.name,
            user = user
        )
    }
}
