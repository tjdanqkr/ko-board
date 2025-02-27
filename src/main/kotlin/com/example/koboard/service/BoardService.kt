package com.example.koboard.service

import com.example.koboard.domain.User
import com.example.koboard.dto.BoardRequest
import com.example.koboard.dto.BoardResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BoardService {
    fun getBoardById(id: Int): BoardResponse
    fun createBoard(request: BoardRequest, me: User): BoardResponse
    fun updateBoard(request: BoardRequest, me: User, id: Int): BoardResponse
    fun deleteBoard(id: Int, me: User)
    fun getAllBoards(pageable: Pageable): Page<BoardResponse>
    fun getMyBoards(me:User): List<BoardResponse>
}