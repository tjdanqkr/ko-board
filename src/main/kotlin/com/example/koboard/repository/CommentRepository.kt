package com.example.koboard.repository

import com.example.koboard.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Int> {
    fun findByIdAndDeletedFalse(id: Int): Comment?
    fun findAllByDeletedFalse(): List<Comment>
    fun findByBoardIdAndDeletedFalse(boardId: Int): List<Comment>
}