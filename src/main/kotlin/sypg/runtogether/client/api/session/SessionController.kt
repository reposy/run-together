package sypg.runtogether.client.api.session

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sypg.runtogether.application.location.RecordLocationUseCase
import sypg.runtogether.application.session.*

/**
 * 세션 관련 REST API Controller
 */
@RestController
@RequestMapping("/api/sessions")
class SessionController(
    private val joinSessionUseCase: JoinSessionUseCase,
    private val startRunningUseCase: StartRunningUseCase,
    private val recordLocationUseCase: RecordLocationUseCase,
    private val finishRunningUseCase: FinishRunningUseCase,
    private val getMyStatsUseCase: GetMyStatsUseCase,
    private val getSessionRankingUseCase: GetSessionRankingUseCase
) {

    /**
     * 세션 참가
     * POST /api/sessions/{sessionId}/join
     */
    @PostMapping("/{sessionId}/join")
    fun joinSession(
        @PathVariable sessionId: Long,
        @RequestBody request: JoinSessionRequest
    ): ResponseEntity<JoinSessionResponse> {
        val participantId = joinSessionUseCase.execute(sessionId, request.userId)
        
        return ResponseEntity.ok(
            JoinSessionResponse(
                participantId = participantId,
                sessionId = sessionId,
                userId = request.userId
            )
        )
    }

    /**
     * 러닝 시작
     * POST /api/sessions/{sessionId}/start
     */
    @PostMapping("/{sessionId}/start")
    fun startRunning(
        @PathVariable sessionId: Long,
        @RequestBody request: StartRunningRequest
    ): ResponseEntity<StartRunningResponse> {
        startRunningUseCase.execute(sessionId, request.userId)
        
        return ResponseEntity.ok(StartRunningResponse())
    }

    /**
     * 위치 기록 (5초마다 호출)
     * POST /api/sessions/{sessionId}/locations
     */
    @PostMapping("/{sessionId}/locations")
    fun recordLocation(
        @PathVariable sessionId: Long,
        @RequestBody request: RecordLocationRequest
    ): ResponseEntity<RecordLocationResponse> {
        val locationId = recordLocationUseCase.execute(
            sessionId = sessionId,
            userId = request.userId,
            latitude = request.latitude,
            longitude = request.longitude
        )
        
        // 업데이트된 통계 조회
        val stats = getMyStatsUseCase.execute(sessionId, request.userId)
        
        return ResponseEntity.ok(
            RecordLocationResponse(
                locationId = locationId,
                totalDistance = stats?.totalDistance ?: 0.0,
                duration = stats?.duration ?: 0L
            )
        )
    }

    /**
     * 내 통계 조회
     * GET /api/sessions/{sessionId}/my-stats?userId={userId}
     */
    @GetMapping("/{sessionId}/my-stats")
    fun getMyStats(
        @PathVariable sessionId: Long,
        @RequestParam userId: Long
    ): ResponseEntity<MyStatsResponse> {
        val stats = getMyStatsUseCase.execute(sessionId, userId)
            ?: return ResponseEntity.notFound().build()
        
        return ResponseEntity.ok(
            MyStatsResponse(
                userId = stats.userId,
                totalDistance = stats.totalDistance,
                duration = stats.duration,
                updatedAt = stats.updatedAt
            )
        )
    }

    /**
     * 세션 순위 조회
     * GET /api/sessions/{sessionId}/ranking?userId={userId}&limit={limit}
     */
    @GetMapping("/{sessionId}/ranking")
    fun getSessionRanking(
        @PathVariable sessionId: Long,
        @RequestParam userId: Long,
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<SessionRankingResponse> {
        // 내 순위
        val myRank = getSessionRankingUseCase.getMyRank(sessionId, userId)
        
        // 내 통계
        val myStats = getMyStatsUseCase.execute(sessionId, userId)?.let {
            MyStatsResponse(
                userId = it.userId,
                totalDistance = it.totalDistance,
                duration = it.duration,
                updatedAt = it.updatedAt
            )
        }
        
        // 상위 순위
        val topRanking = getSessionRankingUseCase.execute(sessionId, limit)
            .mapIndexed { index, stat ->
                RankingEntry(
                    rank = index + 1,
                    userId = stat.userId,
                    totalDistance = stat.totalDistance,
                    duration = stat.duration
                )
            }
        
        return ResponseEntity.ok(
            SessionRankingResponse(
                myRank = myRank,
                myStats = myStats,
                topRanking = topRanking
            )
        )
    }

    /**
     * 러닝 종료
     * POST /api/sessions/{sessionId}/finish
     */
    @PostMapping("/{sessionId}/finish")
    fun finishRunning(
        @PathVariable sessionId: Long,
        @RequestParam userId: Long
    ): ResponseEntity<String> {
        finishRunningUseCase.execute(sessionId, userId)
        return ResponseEntity.ok("Running finished successfully")
    }
}
