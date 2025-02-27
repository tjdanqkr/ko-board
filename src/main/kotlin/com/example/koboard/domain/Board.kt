package com.example.koboard.domain

import com.example.koboard.dto.BoardRequest
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "BOARDS")
class Board(
    @Id @GeneratedValue
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var owner: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @ManyToOne( fetch = FetchType.LAZY)
    val user: User? = null,
    var deleted : Boolean = false,
    var commentCount: Int = 0,
    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val comments: List<Comment> = emptyList(),
){
    fun addComment(comment: Comment) = apply {
        comments.plus(comment)
        commentCount.plus(1)
    }
    fun update(request: BoardRequest) = apply {
        this.name = request.name ?: name
        this.description = request.description ?: description
    }
    fun remove() = apply {
        this.deleted = true
    }
    fun isMe(user: User) = this.user == user

}

