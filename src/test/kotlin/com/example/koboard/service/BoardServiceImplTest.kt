package com.example.koboard.service

import com.example.koboard.domain.Board
import com.example.koboard.domain.Comment
import com.example.koboard.domain.User
import com.example.koboard.dto.BoardRequest
import com.example.koboard.exception.BoardNotFoundException
import com.example.koboard.repository.BoardRepository
import com.example.koboard.repository.UserRepository
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull


@DataJpaTest
class BoardServiceImplTest
@Autowired
constructor(
    private val boardRepository: BoardRepository,
    private val userRepository: UserRepository,
    private val em: EntityManager
) {
    private lateinit var boardService: BoardService
    private lateinit var board: Board
    private lateinit var user: User
    private lateinit var comment: Comment
    @BeforeEach
    fun setUp() {
        user = userRepository.save(
            User(
                name = "test",
                email = "test@test.com",
                _password = "test"
            )
        )
        boardService = BoardServiceImpl(boardRepository)
        board = Board(
            name = "test",
            description = "test",
            owner = "test",
            user = user,
        )
        boardRepository.save(board)
        comment = Comment(
            content = "test",
            owner = "test",
            user = user,
            board = board
        )
        em.persist(comment)
        em.flush()
        em.clear()
    }

    @Nested
    inner class GetBoardById {
        @Test
        fun `should return board by id`() {
            val board = boardService.getBoardById(board.id!!)
            assertEquals("test", board.name)
        }
        @Test
        fun `should throw exception if board not found`() {
            assertThrows(BoardNotFoundException::class.java) {
                boardService.getBoardById(0)
            }.also {
                assertEquals("Board(0) not found", it.message)
            }
        }
    }
    @Nested
    inner class CreateBoard{
        @Test
        fun `should create board`() {
            // given
            val request = BoardRequest(
                name = "test2",
                description = "test2",
            )
            // when
            val user = em.createQuery("select u from User u where id = :id", User::class.java)
                .setParameter("id", user.id)
                .singleResult
            val board = boardService.createBoard(request, user)
            // then
            assertEquals("test2", board.name)
            assertEquals(boardRepository.findAll().size, 2)
        }
    }
    @Nested
    inner class UpdateBoard{
        @Test
        fun `should update board`() {
            // given
            val request = BoardRequest(
                name = "test2",
                description = "test2",
            )
            // when
            val board = boardService.updateBoard(request, user, board.id!!)
            // then
            assertEquals("test2", board.name)

            assertEquals(boardRepository.findByIdOrNull(board.id!!)!!.name, "test2")

        }
        @Test
        fun `should throw exception if board not found`() {
            // given
            val request = BoardRequest(
                name = "test2",
                description = "test2",
            )
            // then
            val assertThrows = assertThrows(BoardNotFoundException::class.java) {
                boardService.updateBoard(request, user, 0)
            }
            assertEquals(assertThrows.message, "Board(0) not found")
        }
    }
    @Nested
    inner class DeleteBoard{
        @Test
        fun `should delete board`() {
            boardService.getAllBoards(Pageable.ofSize(1)).map {
                println(it.id)
            }

            val user = em.createQuery("select u from User u where id = :id", User::class.java)
                .setParameter("id", user.id)
                .singleResult
            boardService.deleteBoard(board.id!!, user)
            val assertThrows = assertThrows(BoardNotFoundException::class.java) {
                boardService.getBoardById(board.id!!)
            }
            assertEquals("Board(${board.id!!}) not found", assertThrows.message)
        }
    }

    @Nested
    inner class GetAllBoards{
        @Test
        fun `should return all boards`() {
            val boards = boardService.getAllBoards(Pageable.ofSize(1))
            assertEquals(1, boards.size)
        }
    }
}