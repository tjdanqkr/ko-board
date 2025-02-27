package com.example.koboard.controller

import com.example.koboard.domain.User
import com.example.koboard.dto.BoardRequest
import com.example.koboard.service.BoardService
import org.springframework.data.domain.Pageable
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
class BoardController(
    private val boardService: BoardService
) {
    @GetMapping("/api/v1/boards")
    fun getAllBoards(pageable: Pageable) = boardService.getAllBoards(pageable)

    @PostMapping("/api/v1/boards")
    fun createBoard(
        @RequestBody request: BoardRequest,
        @AuthenticationPrincipal me: User
    ) = boardService.createBoard(request, me)

    @GetMapping("/api/v1/boards/{id}")
    fun getBoardById(@PathVariable id: Int) = boardService.getBoardById(id)
    @DeleteMapping("/api/v1/boards/{id}")
    fun deleteBoard(
        @PathVariable id: Int,
        @AuthenticationPrincipal me: User
    ) = boardService.deleteBoard(id, me)
    @PutMapping("/api/v1/boards/{id}")
    fun updateBoard(
        @PathVariable id: Int,
        @RequestBody request: BoardRequest,
        @AuthenticationPrincipal me: User
    ) = boardService.updateBoard(request, me, id)
    @GetMapping("/api/v1/boards/mine")
    fun getMyBoards(@AuthenticationPrincipal me: User) = boardService.getMyBoards(me)
}