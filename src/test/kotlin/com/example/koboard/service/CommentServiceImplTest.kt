package com.example.koboard.service

import com.example.koboard.domain.Board
import com.example.koboard.domain.Comment
import com.example.koboard.domain.User
import com.example.koboard.dto.CommentRequest
import com.example.koboard.repository.BoardRepository
import com.example.koboard.repository.CommentRepository
import com.example.koboard.repository.UserRepository
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.assertEquals

@DataJpaTest
class CommentServiceImplTest @Autowired constructor(
    private val commentRepository: CommentRepository,
    private val boardRepository: BoardRepository,
    private val userRepository: UserRepository,
    private val em: EntityManager,
) {
    private lateinit var user: User
    private lateinit var board: Board
    private lateinit var comment: Comment
    private lateinit var commentService: CommentService

    @BeforeEach
    fun setUp() {
        user = userRepository.save(
            User(
                name = "test",
                email = "test@test.com",
                _password = "test"
            )
        )
        board = Board(
            name = "test",
            description = "test",
            owner = "test",
            user = user,
        )
        boardRepository.save(board)
        commentService = CommentServiceImpl(boardRepository, commentRepository)
        comment = Comment(
            content = "test",
            owner = user.name,
            user = user,
            board = board
        )
        commentRepository.save(comment)
        em.flush()
        em.clear()
    }
    @Nested
    inner class GetCommentById {
        @Test
        fun `should return comment by id`() {
            // given
            // when
            val getCommentById = commentService.getCommentById(comment.id!!)
            // then
            assertEquals("test", getCommentById.content)
            assertEquals(user.name, getCommentById.owner)
            assertEquals(board.id, getCommentById.boardId)
        }

        @Test
        fun `should throw exception if comment not found`() {
            // given

            // when
            val assertThrows = assertThrows<IllegalArgumentException> {
                commentService.getCommentById(0)
            }
            // then
            assertEquals("Comment not found", assertThrows.message)
        }
    }
    @Nested
    inner class CreateComment{
        @Test
        fun `should create comment`() {
            // given
            val request = CommentRequest(
                content = "test",
                boardId = board.id!!
            )
            val user = em.createQuery("select u from User u where id = :id", User::class.java)
                .setParameter("id", user.id)
                .singleResult

            // when
            val createComment = commentService.createComment(request, user)

            // then
            assertEquals("test", createComment.content)
            assertEquals(user.name, createComment.owner)
            assertEquals(board.id, createComment.boardId)
        }
        @Test
        fun `should throw exception if content is null`() {
            // given
            val request = CommentRequest(
                content = "",
                boardId = board.id!!
            )
            // then
            val assertThrows = assertThrows<IllegalArgumentException> {
                commentService.createComment(request, user)
            }
            assertEquals("Content must not be blank", assertThrows.message)
        }
        @Test
        fun `should throw exception if board not found`() {
            // given
            val request = CommentRequest(
                content = "test",
                boardId = 0
            )
            // then
            val assertThrows = assertThrows<IllegalArgumentException> {
                commentService.createComment(request, user)
            }
            assertEquals("Board not found", assertThrows.message)
        }
    }

    @Nested
    inner class UpdateComment{
        @Test
        fun `should update comment`() {
            // given
            val request = CommentRequest(
                content = "test2",
                boardId = board.id!!
            )
            // when
            val user = em.createQuery("select u from User u where id = :id", User::class.java)
                .setParameter("id", user.id)
                .singleResult
            val updateComment = commentService.updateComment(request, comment.id!!, user)
            // then
            assertEquals("test2", updateComment.content)
        }
        @Test
        fun `should throw exception if content is null`() {
            // given
            val request = CommentRequest(
                content = "",
                boardId = board.id!!
            )
            val user = em.createQuery("select u from User u where id = :id", User::class.java)
                .setParameter("id", user.id)
                .singleResult

            // then
            assertEquals("test", comment.content)
//            val assertThrows = assertThrows<IllegalArgumentException> {
//                commentService.updateComment(request, comment.id!!, user)
//            }
//            assertEquals("Content must not be blank", assertThrows.message)
        }
        @Test
        fun `should throw exception if comment not found`() {
            // given
            val request = CommentRequest(
                content = "test2",
                boardId = board.id!!
            )
            // then
            val assertThrows = assertThrows<IllegalArgumentException> {
                commentService.updateComment(request, 0, user)
            }
            assertEquals("Comment not found", assertThrows.message)
        }
    }

    @Nested
    inner class DeleteComment{
        @Test
        fun `should delete comment`() {
            // given
            val user = em.createQuery("select u from User u where id = :id", User::class.java)
                .setParameter("id", user.id)
                .singleResult
            // when
            commentService.deleteComment(comment.id!!, user)
            // then
            assertEquals(0, commentRepository.findAllByDeletedFalse().size)
        }
    }
    @Nested
    inner class GetAllComments{
        @Test
        fun `should return all comments`() {
            // given
            val user = em.createQuery("select u from User u where id = :id", User::class.java)
                .setParameter("id", user.id)
                .singleResult
            val board = boardRepository.findByIdOrNull(board.id!!)
            val comment2 = Comment(
                content = "test2",
                owner = user.name,
                user = user,
                board = board
            )
            commentRepository.save(comment2)
            // when
            val allComments = commentService.getAllComments()
            // then
            assertEquals(2, allComments.size)
        }
    }

}