package sypg.runtogether.domain.session

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "user_session_stats",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["session_id", "user_id"])
    ],
    indexes = [
        Index(name = "idx_session_distance", columnList = "session_id,total_distance DESC")
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

    @Column(name = "rank", nullable = true)
    var rank: Int?,

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
                rank = null,
                updatedAt = LocalDateTime.now()
            )
        }
    }

    fun updateStats(distance: Double, durationSeconds: Long) {
        this.totalDistance = distance
        this.duration = durationSeconds
        this.updatedAt = LocalDateTime.now()
    }

    fun updateRank(newRank: Int) {
        this.rank = newRank
    }
}