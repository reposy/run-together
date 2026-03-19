package sypg.runtogether.client.batch

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import sypg.runtogether.application.session.CreateSessionUseCase
import java.time.LocalDateTime

/**
 * 세션 자동 생성 스케줄러
 *
 * 매일 정해진 시간에 러닝 세션을 자동으로 생성합니다.
 */
@Component
class SessionScheduler(
    private val createSessionUseCase: CreateSessionUseCase
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 매일 오전 9시, 오후 8시에 세션 생성
     *
     * - 오전 세션: 9:00 ~ 10:00
     * - 저녁 세션: 20:00 ~ 21:00
     */
    @Scheduled(cron = "0 0 9,20 * * *")
    fun createDailySession() {
        val now = LocalDateTime.now()
        val startAt = now
        val endAt = startAt.plusHours(1)

        try {
            val sessionId = createSessionUseCase.execute(startAt, endAt)
            logger.info("세션 자동 생성 완료: sessionId={}, startAt={}, endAt={}",
                sessionId, startAt, endAt)
        } catch (e: Exception) {
            logger.error("세션 자동 생성 실패: startAt={}, error={}", startAt, e.message, e)
        }
    }

    /**
     * 테스트용: 애플리케이션 시작 후 10초 뒤에 세션 생성
     *
     * 개발/테스트 시에만 사용하고, 프로덕션에서는 주석 처리하세요.
     */
    @Scheduled(fixedDelay = Long.MAX_VALUE, initialDelay = 10000)
    fun createInitialSessionForTest() {
        val startAt = LocalDateTime.now()
        val endAt = startAt.plusHours(1)

        try {
            val sessionId = createSessionUseCase.execute(startAt, endAt)
            logger.info("[TEST] 초기 세션 생성 완료: sessionId={}", sessionId)
        } catch (e: Exception) {
            logger.warn("[TEST] 초기 세션 생성 실패: {}", e.message)
        }
    }
}