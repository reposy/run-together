package sypg.runtogether.domain.user

interface UserRepository {
    fun findById(id: Long): User?
    fun findByUsername(username: String): User?
    fun findByNickname(nickname: String): User?
    fun save(user: User): User
    fun delete(user: User)
}