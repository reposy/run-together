package sypg.runtogether.client.batch

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import sypg.runtogether.domain.session.RunningSessionRepository
import sypg.runtogether.domain.session.SessionStatus
import sypg.runtogether.domain.session.UserSessionStatRepository

/**
 * 랭킹 자동 업데이트 스케줄러
 *
 * 활성 세션들의 참가자 순위를 주기적으로 재계산하여 캐시합니다.
 * 이를 통해 getMyRank() 조회를 O(1)로 처리할 수 있습니다.
 */
@Component
class RankingUpdater(
    private val runningSessionRepository: RunningSessionRepository,
    private val userSessionStatRepository: UserSessionStatRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 10초마다 활성 세션들의 랭킹 업데이트
     *
     * - READY 및 RUNNING 상태 세션만 대상
     * - 거리(totalDistance) 기준 내림차순 정렬
     * - 각 참가자의 rank 필드 업데이트
     */
    @Scheduled(fixedRate = 10000) // 10초마다 실행
    @Transactional
    fun updateRankings() {
        try {
            // 활성 세션 조회 (READY, RUNNING)
            val readySessions = runningSessionRepository.findByStatus(SessionStatus.READY)
            val runningSessions = runningSessionRepository.findByStatus(SessionStatus.RUNNING)
            val activeSessions = readySessions + runningSessions

            if (activeSessions.isEmpty()) {
                return
            }

            var totalUpdated = 0

            // 각 세션별로 랭킹 계산
            activeSessions.forEach { session ->
                val updated = updateSessionRanking(session.id)
                totalUpdated += updated
            }

            if (totalUpdated > 0) {
                logger.debug("랭킹 업데이트 완료: {} 세션, {} 참가자", activeSessions.size, totalUpdated)
            }
        } catch (e: Exception) {
            logger.error("랭킹 업데이트 실패: {}", e.message, e)
        }
    }

    /**
     * 특정 세션의 랭킹 업데이트
     *
     * @param sessionId 세션 ID
     * @return 업데이트된 참가자 수
     */
    private fun updateSessionRanking(sessionId: Long): Int {
        // 해당 세션의 모든 통계를 거리 순으로 조회
        val stats = userSessionStatRepository.findBySessionId(sessionId)
            .sortedByDescending { it.totalDistance }

        if (stats.isEmpty()) {
            return 0
        }

        // 각 참가자에게 순위 할당 (1부터 시작)
        stats.forEachIndexed { index, stat ->
            val newRank = index + 1
            if (stat.rank != newRank) {
                stat.updateRank(newRank)
            }
        }

        // JPA는 @Transactional 내에서 자동으로 변경 감지하여 UPDATE
        return stats.size
    }
}
