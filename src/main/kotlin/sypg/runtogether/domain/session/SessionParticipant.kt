package sypg.runtogether.domain.session

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "session_participants",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["session_id", "user_id"])
    ]
)
class SessionParticipant private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "session_id", nullable = false)
    val sessionId: Long,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: ParticipantStatus,

    @Column(nullable = false)
    val joinedAt: LocalDateTime
) {
    companion object {
        fun join(
            sessionId: Long,
            userId: Long,
            id: Long = 0
        ): SessionParticipant {
            return SessionParticipant(
                id = id,
                sessionId = sessionId,
                userId = userId,
                status = ParticipantStatus.JOINED,
                joinedAt = LocalDateTime.now()
            )
        }
    }

    fun startRunning() {
        require(status == ParticipantStatus.JOINED) { "Participant must be JOINED to start running" }
        status = ParticipantStatus.RUNNING
    }

    fun leave() {
        require(status != ParticipantStatus.FINISHED) { "Cannot leave after finishing" }
        status = ParticipantStatus.LEFT
    }

    fun finish() {
        require(status == ParticipantStatus.RUNNING) { "Participant must be RUNNING to finish" }
        status = ParticipantStatus.FINISHED
    }
}