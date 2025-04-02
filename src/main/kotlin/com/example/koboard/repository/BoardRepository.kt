package com.example.koboard.repository

import com.example.koboard.domain.Board
import com.example.koboard.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BoardRepository : JpaRepository<Board, Int> {
    @Query("SELECT b FROM Board b " +
            "LEFT JOIN FETCH b.comments c " +
            "WHERE b.id = :id AND b.deleted = false")
    fun findByIdAndDeletedFalse(id: Int): Board?
    @Query("SELECT b FROM Board b " +
            "WHERE b.deleted = false")
    fun findAllByDeletedFalse(page: Pageable): Page<Board>
    fun findAllByUserAndDeletedFalse(me: User): List<Board>
}