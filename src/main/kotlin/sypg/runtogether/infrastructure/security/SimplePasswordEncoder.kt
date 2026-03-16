package sypg.runtogether.infrastructure.security

import sypg.runtogether.domain.user.PasswordEncoder

/**
 * PasswordEncoder의 간단한 구현체 (테스트/개발용)
 *
 * 주의: 실제 프로덕션에서는 BCryptPasswordEncoder 사용 권장
 *
 * 프로덕션 예제:
 * import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 *
 * class BCryptPasswordEncoderImpl : PasswordEncoder {
 *     private val encoder = BCryptPasswordEncoder()
 *     override fun encode(rawPassword: String) = encoder.encode(rawPassword)
 *     override fun matches(rawPassword: String, encodedPassword: String) =
 *         encoder.matches(rawPassword, encodedPassword)
 * }
 */
class SimplePasswordEncoder : PasswordEncoder {
    override fun encode(rawPassword: String): String {
        // 테스트용: 단순히 "hashed_" 접두사만 추가
        return "hashed_$rawPassword"
    }

    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return encodedPassword == encode(rawPassword)
    }
}
