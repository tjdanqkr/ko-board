package com.example.koboard.config

import com.example.koboard.domain.Board
import com.example.koboard.domain.User
import com.example.koboard.repository.BoardRepository
import com.example.koboard.repository.UserRepository
import org.springframework.stereotype.Component


@Component
class Init(userRepository: UserRepository, boardRepository: BoardRepository) {

    init {

        val user = userRepository.saveAndFlush(User(name = "test", email = "test@test.com", _password = "test"))
        for(i in 1..100){
            val board = Board(name = "test"+i, description = "test"+i, owner = "test", user = user)
            boardRepository.saveAndFlush(board)
        }

    }

}