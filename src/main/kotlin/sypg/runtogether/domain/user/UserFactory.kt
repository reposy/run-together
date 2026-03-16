package sypg.runtogether.domain.user

class UserFactory(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun create(username: String, password: String, nickname: String): User {
        if (userRepository.findByUsername(username) != null) {
            throw IllegalStateException("username already exists: $username")
        }

        if (userRepository.findByNickname(nickname) != null) {
            throw IllegalStateException("nickname already exists: $nickname")
        }

        val passwordHash = passwordEncoder.encode(password)

        return User.create(
            username = username,
            passwordHash = passwordHash,
            nickname = nickname
        )
    }
}
