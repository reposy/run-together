package sypg.runtogether.domain.session

import java.time.LocalDateTime

class RunnerSessionStat(
    val id: Long,
    val sessionId: Long,
    val runnerId: Long,
    val totalDistance: Double,
    val duration: Long,
    val updatedAt: LocalDateTime,
)