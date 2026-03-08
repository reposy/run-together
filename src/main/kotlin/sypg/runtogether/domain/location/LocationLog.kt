package sypg.runtogether.domain.location

import java.time.LocalDateTime

data class LocationLog (
    val id: Long,
    val runnerId: Long,
    val sessionId: Long,
    val latitude: Double,
    val longitude: Double,
    val recordedAt: LocalDateTime,
)