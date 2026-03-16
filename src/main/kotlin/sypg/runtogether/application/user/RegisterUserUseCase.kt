package sypg.runtogether.application.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sypg.runtogether.domain.user.User
import sypg.runtogether.domain.user.UserFactory
import sypg.runtogether.domain.user.UserRepository

@Service
class RegisterUserUseCase(
    private val userFactory: UserFactory,
    private val userRepository: UserRepository
) {
    @Transactional
    fun execute(username: String, password: String, nickname: String): Long {
        val user = userFactory.create(username, password, nickname)
        val savedUser = userRepository.save(user)
        return savedUser.id
    }

    @Transactional(readOnly = true)
    fun getUser(userId: Long): User? {
        return userRepository.findById(userId)
    }
}
