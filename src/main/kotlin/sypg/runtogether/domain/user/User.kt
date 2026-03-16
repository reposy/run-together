package sypg.runtogether.domain.user

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false, length = 50)
    val username: String,

    @Column(nullable = false)
    val passwordHash: String,

    @Column(unique = true, nullable = false, length = 50)
    val nickname: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime
) {
    companion object {
        fun create(
            username: String,
            passwordHash: String,
            nickname: String,
            id: Long = 0
        ): User {
            require(username.isNotBlank()) { "username must not be blank" }
            require(username.length <= 50) { "username must not exceed 50 characters" }
            require(passwordHash.isNotBlank()) { "passwordHash must not be blank" }
            require(nickname.isNotBlank()) { "nickname must not be blank" }
            require(nickname.length <= 50) { "nickname must not exceed 50 characters" }

            return User(
                id = id,
                username = username,
                passwordHash = passwordHash,
                nickname = nickname,
                createdAt = LocalDateTime.now()
            )
        }
    }
}