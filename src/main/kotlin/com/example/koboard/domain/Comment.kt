package com.example.koboard.domain

import com.example.koboard.dto.CommentRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "COMMENTS")
class Comment(
    @Id @GeneratedValue
    var id: Int? = null,
    var content: String,
    var owner: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var deleted: Boolean = false,
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val board: Board? = null,
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val user: User? = null,
) {
    fun update(request: CommentRequest) = apply {
        this.content = request.content ?: content
    }
    fun remove() = apply {
        this.deleted = true
        val board = this.board?:throw IllegalArgumentException("Board not found")
        board.commentCount = board.commentCount.minus(1)
    }
}