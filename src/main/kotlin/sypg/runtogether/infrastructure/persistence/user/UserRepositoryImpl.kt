package sypg.runtogether.infrastructure.persistence.user

import org.springframework.stereotype.Repository
import sypg.runtogether.domain.user.User
import sypg.runtogether.domain.user.UserRepository

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository
) : UserRepository {

    override fun findById(id: Long): User? {
        return jpaRepository.findById(id).orElse(null)
    }

    override fun findByUsername(username: String): User? {
        return jpaRepository.findByUsername(username)
    }

    override fun findByNickname(nickname: String): User? {
        return jpaRepository.findByNickname(nickname)
    }

    override fun save(user: User): User {
        return jpaRepository.save(user)
    }

    override fun delete(user: User) {
        jpaRepository.delete(user)
    }
}
