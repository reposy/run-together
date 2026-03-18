package sypg.runtogether.domain.location

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "location_logs",
    indexes = [
        Index(name = "idx_session_user", columnList = "session_id,user_id"),
        Index(name = "idx_recorded_at", columnList = "recorded_at")
    ]
)
class LocationLog private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "session_id", nullable = false)
    val sessionId: Long,

    @Column(nullable = false)
    val latitude: Double,

    @Column(nullable = false)
    val longitude: Double,

    @Column(nullable = false)
    val recordedAt: LocalDateTime
) {
    companion object {
        fun record(
            userId: Long,
            sessionId: Long,
            latitude: Double,
            longitude: Double,
            id: Long = 0
        ): LocationLog {
            require(latitude in -90.0..90.0) { "Latitude must be between -90 and 90" }
            require(longitude in -180.0..180.0) { "Longitude must be between -180 and 180" }

            return LocationLog(
                id = id,
                userId = userId,
                sessionId = sessionId,
                latitude = latitude,
                longitude = longitude,
                recordedAt = LocalDateTime.now()
            )
        }
    }
}