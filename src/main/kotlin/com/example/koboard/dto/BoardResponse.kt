package com.example.koboard.dto

import com.example.koboard.domain.Board
import java.time.LocalDateTime

data class BoardResponse(
    val id: Int,
    val name: String,
    val description: String,
    val owner: String,
    val ownerId: Int,
    val createdAt: LocalDateTime,
    val commentCount: Int
) {
    companion object{
        fun from(board: Board): BoardResponse {
            return BoardResponse(
                id = board.id!!,
                name = board.name.orEmpty(),
                description = board.description.orEmpty(),
                owner = board.owner.orEmpty(),
                ownerId = board.user?.id!!,
                createdAt = board.createdAt,
                commentCount = board.comments.size
            )
        }
    }
}