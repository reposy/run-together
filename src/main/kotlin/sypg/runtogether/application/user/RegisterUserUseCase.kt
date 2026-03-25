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
    /**
     * 닉네임만으로 간단 회원가입
     */
    @Transactional
    fun execute(nickname: String): Long {
        // username = nickname, password = "temp_password"로 간단하게 처리
        val user = userFactory.create(
            username = nickname,
            password = "temp_password",
            nickname = nickname
        )
        val savedUser = userRepository.save(user)
        return savedUser.id
    }

    /**
     * 기존 방식 호환 (username, password, nickname 모두 입력)
     */
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
