package sypg.runtogether.application.session

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sypg.runtogether.domain.session.UserSessionStat
import sypg.runtogether.domain.session.UserSessionStatRepository

/**
 * 내 통계 조회 UseCase
 * 
 * 특정 세션에서 내 러닝 통계를 조회
 */
@Service
class GetMyStatsUseCase(
    private val userSessionStatRepository: UserSessionStatRepository
) {
    /**
     * 내 통계 조회
     * 
     * @param sessionId 세션 ID
     * @param userId 사용자 ID
     * @return 내 통계 (없으면 null)
     */
    @Transactional(readOnly = true)
    fun execute(sessionId: Long, userId: Long): UserSessionStat? {
        return userSessionStatRepository.findBySessionIdAndUserId(sessionId, userId)
    }
}
