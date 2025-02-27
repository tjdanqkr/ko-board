package com.example.koboard.service

import com.example.koboard.domain.User
import com.example.koboard.dto.CommentRequest
import com.example.koboard.dto.CommentResponse

interface CommentService {
    fun getCommentById(id: Int): CommentResponse
    fun createComment(request: CommentRequest, user: User): CommentResponse
    fun updateComment(request: CommentRequest, commentId: Int, user: User): CommentResponse
    fun deleteComment(id: Int, user: User): CommentResponse
    fun getAllComments(): List<CommentResponse>
    fun getCommentsByBoardId(boardId: Int): List<CommentResponse>
}