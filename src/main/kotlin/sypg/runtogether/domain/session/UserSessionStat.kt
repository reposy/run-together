package sypg.runtogether.domain.session

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "user_session_stats",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["session_id", "user_id"])
    ]
)
class UserSessionStat private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "session_id", nullable = false)
    val sessionId: Long,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(nullable = false)
    var totalDistance: Double,

    @Column(nullable = false)
    var duration: Long,

    @Column(nullable = false)
    var updatedAt: LocalDateTime
) {
    companion object {
        fun create(
            sessionId: Long,
            userId: Long,
            id: Long = 0
        ): UserSessionStat {
            return UserSessionStat(
                id = id,
                sessionId = sessionId,
                userId = userId,
                totalDistance = 0.0,
                duration = 0L,
                updatedAt = LocalDateTime.now()
            )
        }
    }

    fun updateStats(distance: Double, durationSeconds: Long) {
        this.totalDistance = distance
        this.duration = durationSeconds
        this.updatedAt = LocalDateTime.now()
    }
}