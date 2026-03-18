package sypg.runtogether.domain.session

import java.time.LocalDateTime

class UserSessionStat(
    val id: Long,
    val sessionId: Long,
    val userId: Long,
    val totalDistance: Double,
    val duration: Long,
    val updatedAt: LocalDateTime,
)