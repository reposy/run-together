package sypg.runtogether.client.batch

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import sypg.runtogether.domain.session.RunningSessionRepository
import sypg.runtogether.domain.session.SessionStatus
import java.time.LocalDateTime

/**
 * 세션 상태 자동 업데이트 스케줄러
 *
 * 세션의 시작/종료 시간에 맞춰 상태를 자동으로 전환합니다.
 * - READY → RUNNING (시작 시간 도달)
 * - RUNNING → FINISHED (종료 시간 도달)
 */
@Component
class SessionStatusUpdater(
    private val runningSessionRepository: RunningSessionRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 1분마다 세션 상태 확인 및 업데이트
     */
    @Scheduled(fixedRate = 60000) // 60000ms = 1분
    @Transactional
    fun updateSessionStatus() {
        val now = LocalDateTime.now()

        try {
            // READY 상태인 세션 중 시작 시간이 된 것들을 RUNNING으로 전환
            val readySessions = runningSessionRepository.findByStatus(SessionStatus.READY)
            val sessionsToStart = readySessions.filter { it.startAt <= now }

            sessionsToStart.forEach { session ->
                session.start()
                runningSessionRepository.save(session)
                logger.info("세션 시작: sessionId={}, startAt={}", session.id, session.startAt)
            }

            // RUNNING 상태인 세션 중 종료 시간이 된 것들을 FINISHED로 전환
            val runningSessions = runningSessionRepository.findByStatus(SessionStatus.RUNNING)
            val sessionsToFinish = runningSessions.filter { it.endAt <= now }

            sessionsToFinish.forEach { session ->
                session.finish()
                runningSessionRepository.save(session)
                logger.info("세션 종료: sessionId={}, endAt={}", session.id, session.endAt)
            }

            if (sessionsToStart.isNotEmpty() || sessionsToFinish.isNotEmpty()) {
                logger.info("세션 상태 업데이트 완료: 시작={}, 종료={}",
                    sessionsToStart.size, sessionsToFinish.size)
            }
        } catch (e: Exception) {
            logger.error("세션 상태 업데이트 실패: error={}", e.message, e)
        }
    }
}