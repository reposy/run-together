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
     * 매일 자정에 전일 세션 생성
     *
     * - 세션 시간: 00:00:00 ~ 23:59:59 (하루 종일)
     * - 매일 참가자 데이터 초기화됨
     */
    @Scheduled(cron = "0 0 0 * * *")
    fun createDailySession() {
        val now = LocalDateTime.now()
        val startAt = now.toLocalDate().atStartOfDay()  // 00:00:00
        val endAt = startAt.plusDays(1).minusSeconds(1)  // 23:59:59

        try {
            val sessionId = createSessionUseCase.execute(startAt, endAt)
            logger.info("전일 세션 자동 생성 완료: sessionId={}, startAt={}, endAt={}",
                sessionId, startAt, endAt)
        } catch (e: Exception) {
            logger.error("전일 세션 자동 생성 실패: startAt={}, error={}", startAt, e.message, e)
        }
    }

    /**
     * 테스트용: 애플리케이션 시작 직후 전일 세션 생성
     *
     * - 세션 시간: 00:00:00 ~ 23:59:59 (당일 전체)
     * - 개발/테스트 시에만 사용하고, 프로덕션에서는 주석 처리하세요.
     */
    @Scheduled(fixedDelay = Long.MAX_VALUE, initialDelay = 1000)
    fun createInitialSessionForTest() {
        val now = LocalDateTime.now()
        val startAt = now.toLocalDate().atStartOfDay()  // 00:00:00
        val endAt = startAt.plusDays(1).minusSeconds(1)  // 23:59:59

        try {
            val sessionId = createSessionUseCase.execute(startAt, endAt)
            logger.info("[TEST] 전일 세션 생성 완료: sessionId={}, startAt={}, endAt={}",
                sessionId, startAt, endAt)
        } catch (e: Exception) {
            logger.warn("[TEST] 전일 세션 생성 실패: {}", e.message)
        }
    }
}