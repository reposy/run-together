package sypg.runtogether.application.session

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sypg.runtogether.domain.session.UserSessionStat
import sypg.runtogether.domain.session.UserSessionStatRepository

/**
 * 세션 순위 조회 UseCase
 * 
 * 특정 세션의 전체 참가자 순위를 거리 기준으로 조회
 */
@Service
class GetSessionRankingUseCase(
    private val userSessionStatRepository: UserSessionStatRepository
) {
    /**
     * 세션 순위 조회 (거리 순)
     * 
     * @param sessionId 세션 ID
     * @param limit 조회할 순위 수 (기본 100명)
     * @return 순위별 통계 리스트
     */
    @Transactional(readOnly = true)
    fun execute(sessionId: Long, limit: Int = 100): List<UserSessionStat> {
        require(limit > 0) { "Limit must be greater than 0" }
        
        return userSessionStatRepository.findTopBySessionIdOrderByTotalDistanceDesc(sessionId, limit)
    }
    
    /**
     * 내 순위 조회 (캐시된 rank 필드 사용 - O(1))
     *
     * rank 필드는 RankingUpdater 스케줄러에 의해 10초마다 업데이트됨
     *
     * @param sessionId 세션 ID
     * @param userId 사용자 ID
     * @return 내 순위 (1부터 시작, 없으면 null)
     */
    @Transactional(readOnly = true)
    fun getMyRank(sessionId: Long, userId: Long): Int? {
        val stat = userSessionStatRepository.findBySessionIdAndUserId(sessionId, userId)
            ?: return null

        return stat.rank
    }
}
