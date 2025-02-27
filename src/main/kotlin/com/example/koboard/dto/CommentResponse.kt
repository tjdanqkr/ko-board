package com.example.koboard.dto

import com.example.koboard.domain.Comment
import java.time.LocalDateTime

data class CommentResponse(
    val id: Int,
    val content: String,
    val owner: String,
    val createdAt: LocalDateTime,
    val boardId: Int,
    val ownerId: Int
) {
    companion object {
        fun from(comment: Comment): CommentResponse {
            return CommentResponse(
                id = comment.id!!,
                content = comment.content,
                owner = comment.owner,
                createdAt = comment.createdAt,
                boardId = comment.board?.id!!,
                ownerId = comment.user?.id!!
            )
        }
    }
}