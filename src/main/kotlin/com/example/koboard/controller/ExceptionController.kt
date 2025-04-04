package com.example.koboard.controller

import com.example.koboard.exception.BoardNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionController {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BoardNotFoundException::class)
    fun handleBoardNotFoundException(e: BoardNotFoundException): Map<String, String> {
        return mapOf(
            "error" to e.message.toString(),
            "status" to "404"
        )
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): Map<String, String> {
        return mapOf(
            "error" to e.message.toString(),
            "status" to "400"
        )
    }
}