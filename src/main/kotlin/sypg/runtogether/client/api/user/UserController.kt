package sypg.runtogether.client.api.user

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sypg.runtogether.application.user.RegisterUserUseCase
import sypg.runtogether.domain.user.UserRepository

/**
 * 사용자 관련 REST API Controller
 */
@RestController
@RequestMapping("/api/users")
class UserController(
    private val registerUserUseCase: RegisterUserUseCase,
    private val userRepository: UserRepository
) {

    /**
     * 닉네임으로 간단 회원가입
     * POST /api/users/register
     */
    @PostMapping("/register")
    fun registerUser(
        @RequestBody request: RegisterUserRequest
    ): ResponseEntity<UserResponse> {
        val userId = registerUserUseCase.execute(
            nickname = request.nickname
        )

        return ResponseEntity.ok(
            UserResponse(
                userId = userId,
                nickname = request.nickname
            )
        )
    }

    /**
     * 사용자 정보 조회
     * GET /api/users/{userId}
     */
    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: Long
    ): ResponseEntity<UserResponse> {
        val user = userRepository.findById(userId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(
            UserResponse(
                userId = user.id,
                nickname = user.nickname
            )
        )
    }

    /**
     * 닉네임 중복 체크
     * GET /api/users/check-nickname?nickname=홍길동
     */
    @GetMapping("/check-nickname")
    fun checkNickname(
        @RequestParam nickname: String
    ): ResponseEntity<CheckNicknameResponse> {
        val existingUser = userRepository.findByNickname(nickname)
        val available = existingUser == null

        return ResponseEntity.ok(
            CheckNicknameResponse(available = available)
        )
    }
}
