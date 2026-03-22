package sypg.runtogether.client.api.session

import java.time.LocalDateTime

/**
 * 세션 참가 요청
 */
data class JoinSessionRequest(
    val userId: Long
)

/**
 * 세션 참가 응답
 */
data class JoinSessionResponse(
    val participantId: Long,
    val sessionId: Long,
    val userId: Long,
    val message: String = "Successfully joined session"
)

/**
 * 러닝 시작 요청
 */
data class StartRunningRequest(
    val userId: Long
)

/**
 * 러닝 시작 응답
 */
data class StartRunningResponse(
    val message: String = "Running started"
)

/**
 * 위치 기록 요청
 */
data class RecordLocationRequest(
    val userId: Long,
    val latitude: Double,
    val longitude: Double
)

/**
 * 위치 기록 응답
 */
data class RecordLocationResponse(
    val locationId: Long,
    val totalDistance: Double,
    val duration: Long,
    val message: String = "Location recorded"
)

/**
 * 내 통계 응답
 */
data class MyStatsResponse(
    val userId: Long,
    val totalDistance: Double,
    val duration: Long,
    val updatedAt: LocalDateTime
)

/**
 * 순위 정보
 */
data class RankingEntry(
    val rank: Int,
    val userId: Long,
    val totalDistance: Double,
    val duration: Long
)

/**
 * 세션 순위 응답
 */
data class SessionRankingResponse(
    val myRank: Int?,
    val myStats: MyStatsResponse?,
    val topRanking: List<RankingEntry>
)

/**
 * 세션 정보 응답
 */
data class SessionResponse(
    val id: Long,
    val status: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val participantCount: Int
)
