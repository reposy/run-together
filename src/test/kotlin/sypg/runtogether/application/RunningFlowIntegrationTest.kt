package sypg.runtogether.application

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import sypg.runtogether.application.location.RecordLocationUseCase
import sypg.runtogether.application.session.*
import sypg.runtogether.application.user.RegisterUserUseCase
import sypg.runtogether.domain.session.SessionStatus
import java.time.LocalDateTime

/**
 * 핵심 비즈니스 흐름 통합 테스트
 *
 * 테스트 시나리오:
 * 1. 사용자 2명 생성
 * 2. 세션 생성
 * 3. 두 사용자 모두 세션 참가
 * 4. 러닝 시작
 * 5. 위치 기록 (user1이 더 많이 이동)
 * 6. 순위 확인 (user1이 1위)
 */
@SpringBootTest
@Transactional
class RunningFlowIntegrationTest {

    @Autowired
    private lateinit var registerUserUseCase: RegisterUserUseCase

    @Autowired
    private lateinit var createSessionUseCase: CreateSessionUseCase

    @Autowired
    private lateinit var joinSessionUseCase: JoinSessionUseCase

    @Autowired
    private lateinit var startRunningUseCase: StartRunningUseCase

    @Autowired
    private lateinit var recordLocationUseCase: RecordLocationUseCase

    @Autowired
    private lateinit var getMyStatsUseCase: GetMyStatsUseCase

    @Autowired
    private lateinit var getSessionRankingUseCase: GetSessionRankingUseCase

    @Autowired
    private lateinit var finishRunningUseCase: FinishRunningUseCase

    private var user1Id: Long = 0
    private var user2Id: Long = 0
    private var sessionId: Long = 0

    @BeforeEach
    fun setUp() {
        // 1. 사용자 2명 생성
        user1Id = registerUserUseCase.execute("runner1", "password123", "빠른러너")
        user2Id = registerUserUseCase.execute("runner2", "password456", "느린러너")

        // 2. 세션 생성 (1시간 후 시작, 2시간 후 종료)
        val startAt = LocalDateTime.now().plusHours(1)
        val endAt = startAt.plusHours(1)
        sessionId = createSessionUseCase.execute(startAt, endAt)
    }

    @Test
    fun `전체 러닝 흐름 테스트 - 참가, 시작, 위치 기록, 순위 조회`() {
        // 3. 두 사용자 세션 참가
        val participant1Id = joinSessionUseCase.execute(sessionId, user1Id)
        val participant2Id = joinSessionUseCase.execute(sessionId, user2Id)

        assertNotNull(participant1Id)
        assertNotNull(participant2Id)

        // 4. 러닝 시작
        startRunningUseCase.execute(sessionId, user1Id)
        startRunningUseCase.execute(sessionId, user2Id)

        // 5. 위치 기록 - user1이 더 많이 이동
        // User1: 서울 시청 근처에서 이동 (약 500m)
        recordLocationUseCase.execute(sessionId, user1Id, 37.5665, 126.9780) // 시작점
        Thread.sleep(100) // 시간 차이를 위해
        recordLocationUseCase.execute(sessionId, user1Id, 37.5700, 126.9780) // 북쪽으로 이동 (~390m)
        Thread.sleep(100)
        recordLocationUseCase.execute(sessionId, user1Id, 37.5710, 126.9780) // 추가 이동 (~110m)

        // User2: 짧은 거리만 이동 (약 100m)
        recordLocationUseCase.execute(sessionId, user2Id, 37.5665, 126.9780) // 시작점
        Thread.sleep(100)
        recordLocationUseCase.execute(sessionId, user2Id, 37.5675, 126.9780) // 짧게 이동 (~110m)

        // 6. 통계 확인
        val user1Stats = getMyStatsUseCase.execute(sessionId, user1Id)
        val user2Stats = getMyStatsUseCase.execute(sessionId, user2Id)

        assertNotNull(user1Stats)
        assertNotNull(user2Stats)

        // User1이 더 많이 이동했는지 확인
        assertTrue(user1Stats!!.totalDistance > user2Stats!!.totalDistance,
            "User1(${user1Stats.totalDistance}m) should have run more than User2(${user2Stats.totalDistance}m)")

        println("User1 distance: ${user1Stats.totalDistance}m")
        println("User2 distance: ${user2Stats.totalDistance}m")

        // 7. 순위 확인
        val ranking = getSessionRankingUseCase.execute(sessionId, limit = 10)

        assertEquals(2, ranking.size)
        assertEquals(user1Id, ranking[0].userId, "User1 should be ranked 1st")
        assertEquals(user2Id, ranking[1].userId, "User2 should be ranked 2nd")

        // 내 순위 확인
        val user1Rank = getSessionRankingUseCase.getMyRank(sessionId, user1Id)
        val user2Rank = getSessionRankingUseCase.getMyRank(sessionId, user2Id)

        assertEquals(1, user1Rank)
        assertEquals(2, user2Rank)

        println("Ranking verified: User1=1st, User2=2nd")
    }

    @Test
    fun `중복 참가 방지 테스트`() {
        // 첫 번째 참가 성공
        joinSessionUseCase.execute(sessionId, user1Id)

        // 두 번째 참가 시도 - 예외 발생 예상
        assertThrows(IllegalArgumentException::class.java) {
            joinSessionUseCase.execute(sessionId, user1Id)
        }
    }

    @Test
    fun `러닝 시작 전 위치 기록 불가 테스트`() {
        // 참가만 하고 러닝 시작 안 함
        joinSessionUseCase.execute(sessionId, user1Id)

        // 위치 기록 시도 - 예외 발생 예상
        assertThrows(IllegalArgumentException::class.java) {
            recordLocationUseCase.execute(sessionId, user1Id, 37.5665, 126.9780)
        }
    }

    @Test
    fun `러닝 종료 후 통계 확인`() {
        // 참가 → 시작 → 위치 기록 → 종료
        joinSessionUseCase.execute(sessionId, user1Id)
        startRunningUseCase.execute(sessionId, user1Id)

        recordLocationUseCase.execute(sessionId, user1Id, 37.5665, 126.9780)
        Thread.sleep(100)
        recordLocationUseCase.execute(sessionId, user1Id, 37.5700, 126.9780)

        val statsBeforeFinish = getMyStatsUseCase.execute(sessionId, user1Id)
        assertNotNull(statsBeforeFinish)
        assertTrue(statsBeforeFinish!!.totalDistance > 0)

        // 종료
        finishRunningUseCase.execute(sessionId, user1Id)

        // 종료 후에도 통계는 유지되어야 함
        val statsAfterFinish = getMyStatsUseCase.execute(sessionId, user1Id)
        assertNotNull(statsAfterFinish)
        assertEquals(statsBeforeFinish.totalDistance, statsAfterFinish!!.totalDistance)
    }
}
