package com.example.koboard.service

import com.example.koboard.domain.User
import com.example.koboard.dto.BoardRequest
import com.example.koboard.dto.BoardResponse
import com.example.koboard.dto.toEntity
import com.example.koboard.exception.BoardNotFoundException
import com.example.koboard.repository.BoardRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardServiceImpl(
    private val boardRepository: BoardRepository,
): BoardService {
    override fun getBoardById(id: Int): BoardResponse =
        boardRepository.findByIdAndDeletedFalse(id)
            ?.let { BoardResponse.from(it) }
            ?: throw BoardNotFoundException(id)
    @Transactional
    override fun createBoard(request: BoardRequest, me: User): BoardResponse =
        request.toEntity(me)
            .let { boardRepository.save(it) }
            .let { BoardResponse.from(it) }
    @Transactional
    override fun updateBoard(request: BoardRequest, me: User, id: Int): BoardResponse =
        boardRepository.findByIdAndDeletedFalse(id)
            ?.apply { update(request) }
            ?.let { BoardResponse.from(it) }
            ?: throw BoardNotFoundException(id)

    @Transactional
    override fun deleteBoard(id: Int, me: User) {
        val board = boardRepository.findByIdAndDeletedFalse(id) ?: throw BoardNotFoundException(id)
        when {
            board.user?.id != me.id -> {
                throw IllegalArgumentException("You are not the owner of this board")
            }
            else -> board.remove()
        }
    }

    override fun getAllBoards(pageable: Pageable): Page<BoardResponse> =
        boardRepository.findAllByDeletedFalse(pageable)
            .map { BoardResponse.from(it) }

    override fun getMyBoards(me: User): List<BoardResponse> {
        return boardRepository.findAllByUserAndDeletedFalse(me)
            .map { BoardResponse.from(it) }
    }
}