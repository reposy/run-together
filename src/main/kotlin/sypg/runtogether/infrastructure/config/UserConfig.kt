package sypg.runtogether.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import sypg.runtogether.domain.user.PasswordEncoder
import sypg.runtogether.domain.user.UserFactory
import sypg.runtogether.domain.user.UserRepository
import sypg.runtogether.infrastructure.security.SimplePasswordEncoder

/**
 * User 도메인 관련 Bean 설정
 *
 * 역할:
 * - 도메인 객체들을 Spring Bean으로 등록
 * - 도메인 계층은 Spring에 의존하지 않고, Infrastructure에서 연결
 */
@Configuration
class UserConfig {

    /**
     * UserFactory를 Spring Bean으로 등록
     *
     * Spring이 자동으로:
     * 1. UserRepository 구현체(UserRepositoryImpl) 주입
     * 2. PasswordEncoder 구현체(SimplePasswordEncoder) 주입
     */
    @Bean
    fun userFactory(
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder
    ): UserFactory {
        return UserFactory(userRepository, passwordEncoder)
    }

    /**
     * PasswordEncoder 구현체를 Spring Bean으로 등록
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return SimplePasswordEncoder()
    }
}
