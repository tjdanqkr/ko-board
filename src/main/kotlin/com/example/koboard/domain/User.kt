package com.example.koboard.domain


import com.example.koboard.dto.UserRequest
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Entity
@Table(name = "USERS")
data class User(
    @Id @GeneratedValue var id: Int? = null,
    @Column(name = "name", unique = true)
    var name: String,
    @Column(name = "email", unique = true)
    var email: String,
    var _password: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val boards: List<Board> = emptyList(),
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val comments: List<Comment> = emptyList()
) : UserDetails{
    fun update(request: UserRequest) = apply {
        name = request.name ?: name
        email = request.email ?: email
        _password = request.password ?: _password
    }
    fun addBoard(board: Board) = apply {
        boards.plus(board)
    }
    fun addComment(comment: Comment) = apply {
        comments.plus(comment)
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun getPassword(): String = _password

    override fun getUsername(): String = name



}


