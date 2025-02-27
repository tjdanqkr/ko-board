package com.example.koboard.repository

import com.example.koboard.domain.Board
import com.example.koboard.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BoardRepository : JpaRepository<Board, Int> {
    @Query("SELECT b FROM Board b " +
            "JOIN FETCH b.comments c " +
            "WHERE b.id = :id AND b.deleted = false AND c.deleted = false")
    fun findByIdAndDeletedFalse(id: Int): Board?
    fun findAllByDeletedFalse(page: Pageable): Page<Board>
    fun findAllByUserAndDeletedFalse(me: User): List<Board>
}