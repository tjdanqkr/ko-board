package com.example.koboard.dto

import com.example.koboard.domain.Board
import com.example.koboard.domain.Comment
import com.example.koboard.domain.User

data class CommentRequest(
    val content: String? = null,
    val boardId: Int,
)
fun CommentRequest.toEntity(
    user: User, board: Board
):Comment{
    when {
        content.isNullOrBlank() -> throw IllegalArgumentException("Content must not be blank")
        else -> return Comment(
            id = null,
            content = content,
            owner = user.name,
            user = user,
            board = board
        )
    }

}
