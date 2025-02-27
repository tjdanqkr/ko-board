package com.example.koboard.service

import com.example.koboard.domain.User
import com.example.koboard.dto.CommentRequest
import com.example.koboard.dto.CommentResponse
import com.example.koboard.dto.toEntity
import com.example.koboard.repository.BoardRepository
import com.example.koboard.repository.CommentRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    private val boardRepository: BoardRepository,
    private val commentRepository: CommentRepository,
) : CommentService {
    override fun getCommentById(id: Int): CommentResponse =
        commentRepository.findByIdAndDeletedFalse(id)
            ?.let { CommentResponse.from(it) }
            ?: throw IllegalArgumentException("Comment not found")
    @Transactional
    override fun createComment(request: CommentRequest, user: User): CommentResponse =
        boardRepository.findByIdAndDeletedFalse(request.boardId)
            ?.let { it.addComment(request.toEntity(user, it)) }
            ?.let { commentRepository.save(it.comments[it.comments.size-1]) }
            ?.let { CommentResponse.from(it) }
            ?: throw IllegalArgumentException("Board not found")

    @Transactional
    override fun updateComment(request: CommentRequest, commentId: Int, user: User): CommentResponse {
        val comment = commentRepository.findByIdAndDeletedFalse(commentId) ?: throw IllegalArgumentException("Comment not found")
        return when{
            comment.user !== user -> throw IllegalArgumentException("You are not the owner of this comment")
            else -> CommentResponse.from(comment.update(request))
        }
    }

    @Transactional
    override fun deleteComment(id: Int, user: User): CommentResponse {
        val comment =
            commentRepository.findByIdAndDeletedFalse(id) ?: throw IllegalArgumentException("Comment not found")
        if (comment.user !== user) throw IllegalArgumentException("You are not the owner of this comment")
        return comment.apply { remove() }
            .let { CommentResponse.from(it) }
    }

    override fun getAllComments(): List<CommentResponse> =
        commentRepository.findAllByDeletedFalse()
            .map { CommentResponse.from(it) }

    override fun getCommentsByBoardId(boardId: Int): List<CommentResponse> =
        commentRepository.findByBoardIdAndDeletedFalse(boardId)
            .map { CommentResponse.from(it) }

}