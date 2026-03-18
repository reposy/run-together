package sypg.runtogether.infrastructure.persistence.session

import org.springframework.stereotype.Repository
import sypg.runtogether.domain.session.RunningSession
import sypg.runtogether.domain.session.RunningSessionRepository
import sypg.runtogether.domain.session.SessionStatus
import java.time.LocalDateTime

@Repository
class RunningSessionRepositoryImpl(
    private val jpaRepository: RunningSessionJpaRepository
) : RunningSessionRepository {

    override fun findById(id: Long): RunningSession? {
        return jpaRepository.findById(id).orElse(null)
    }

    override fun findAll(): List<RunningSession> {
        return jpaRepository.findAll()
    }

    override fun findByStatus(status: SessionStatus): List<RunningSession> {
        return jpaRepository.findByStatus(status)
    }

    override fun findActiveSessionsAt(time: LocalDateTime): List<RunningSession> {
        return jpaRepository.findActiveSessionsAt(time)
    }

    override fun save(session: RunningSession): RunningSession {
        return jpaRepository.save(session)
    }

    override fun delete(session: RunningSession) {
        jpaRepository.delete(session)
    }
}
