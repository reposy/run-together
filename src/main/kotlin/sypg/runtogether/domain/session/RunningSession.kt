package sypg.runtogether.domain.session

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "running_sessions")
class RunningSession private constructor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: SessionStatus,

    @Column(nullable = false)
    val startAt: LocalDateTime,

    @Column(nullable = false)
    val endAt: LocalDateTime,

    @Column(nullable = false)
    val createdAt: LocalDateTime
) {
    companion object {
        fun create(
            startAt: LocalDateTime,
            endAt: LocalDateTime,
            status: SessionStatus = SessionStatus.READY,
            id: Long = 0
        ): RunningSession {
            require(endAt.isAfter(startAt)) { "endAt must be after startAt" }

            return RunningSession(
                id = id,
                status = status,
                startAt = startAt,
                endAt = endAt,
                createdAt = LocalDateTime.now()
            )
        }
    }

    fun start() {
        require(status == SessionStatus.READY) { "Session must be READY to start" }
        status = SessionStatus.RUNNING
    }

    fun finish() {
        require(status == SessionStatus.RUNNING) { "Session must be RUNNING to finish" }
        status = SessionStatus.FINISHED
    }
}