package sypg.runtogether.client.batch

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import sypg.runtogether.domain.session.RunningSessionRepository
import sypg.runtogether.domain.session.SessionStatus

/**
 * SessionScheduler 테스트
 *
 * 배치 스케줄러가 세션을 정상적으로 생성하는지 검증
 */
@SpringBootTest
@Transactional
class SessionSchedulerTest {

    @Autowired
    private lateinit var sessionScheduler: SessionScheduler

    @Autowired
    private lateinit var runningSessionRepository: RunningSessionRepository

    @Test
    fun `배치로 세션 생성 성공`() {
        // 초기 세션 개수 확인
        val initialCount = runningSessionRepository.findAll().size

        // 배치 실행
        sessionScheduler.createDailySession()

        // 세션이 1개 증가했는지 확인
        val afterCount = runningSessionRepository.findAll().size
        assertEquals(initialCount + 1, afterCount)

        // 생성된 세션 확인
        val sessions = runningSessionRepository.findAll()
        val latestSession = sessions.maxByOrNull { it.createdAt }

        assertNotNull(latestSession)
        assertEquals(SessionStatus.READY, latestSession!!.status)

        // 시작 시간과 종료 시간이 1시간 차이인지 확인
        val duration = java.time.Duration.between(latestSession.startAt, latestSession.endAt)
        assertEquals(3600L, duration.seconds) // 1시간 = 3600초

        println("세션 생성 확인: sessionId=${latestSession.id}, status=${latestSession.status}")
    }

    @Test
    fun `여러 번 배치 실행해도 각각 세션 생성`() {
        val initialCount = runningSessionRepository.findAll().size

        // 배치 3번 실행
        repeat(3) {
            sessionScheduler.createDailySession()
            Thread.sleep(100) // createdAt 차이를 두기 위해
        }

        val afterCount = runningSessionRepository.findAll().size
        assertEquals(initialCount + 3, afterCount)

        println("세션 3개 생성 확인: ${afterCount}개")
    }
}