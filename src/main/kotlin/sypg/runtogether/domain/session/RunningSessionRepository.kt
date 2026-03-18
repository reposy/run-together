package sypg.runtogether.domain.session

import java.time.LocalDateTime

interface RunningSessionRepository {
    fun findById(id: Long): RunningSession?
    fun findAll(): List<RunningSession>
    fun findByStatus(status: SessionStatus): List<RunningSession>
    fun findActiveSessionsAt(time: LocalDateTime): List<RunningSession>
    fun save(session: RunningSession): RunningSession
    fun delete(session: RunningSession)
}