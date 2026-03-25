package sypg.runtogether.client.api.user

/**
 * 닉네임으로 회원가입 요청
 */
data class RegisterUserRequest(
    val nickname: String
)

/**
 * 사용자 정보 응답
 */
data class UserResponse(
    val userId: Long,
    val nickname: String
)

/**
 * 닉네임 중복 체크 응답
 */
data class CheckNicknameResponse(
    val available: Boolean,
    val message: String = if (available) "사용 가능한 닉네임입니다" else "이미 사용 중인 닉네임입니다"
)
