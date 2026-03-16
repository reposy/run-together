package sypg.runtogether.infrastructure.persistence.user

import org.springframework.data.jpa.repository.JpaRepository
import sypg.runtogether.domain.user.User

interface UserJpaRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun findByNickname(nickname: String): User?
}
