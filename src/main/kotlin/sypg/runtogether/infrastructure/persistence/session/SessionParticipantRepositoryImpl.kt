package sypg.runtogether.infrastructure.persistence.session

import org.springframework.stereotype.Repository
import sypg.runtogether.domain.session.SessionParticipant
import sypg.runtogether.domain.session.SessionParticipantRepository

@Repository
class SessionParticipantRepositoryImpl(
    private val jpaRepository: SessionParticipantJpaRepository
) : SessionParticipantRepository {

    override fun findById(id: Long): SessionParticipant? {
        return jpaRepository.findById(id).orElse(null)
    }

    override fun findBySessionId(sessionId: Long): List<SessionParticipant> {
        return jpaRepository.findBySessionId(sessionId)
    }

    override fun findByUserId(userId: Long): List<SessionParticipant> {
        return jpaRepository.findByUserId(userId)
    }

    override fun findBySessionIdAndUserId(sessionId: Long, userId: Long): SessionParticipant? {
        return jpaRepository.findBySessionIdAndUserId(sessionId, userId)
    }

    override fun existsBySessionIdAndUserId(sessionId: Long, userId: Long): Boolean {
        return jpaRepository.existsBySessionIdAndUserId(sessionId, userId)
    }

    override fun save(participant: SessionParticipant): SessionParticipant {
        return jpaRepository.save(participant)
    }

    override fun delete(participant: SessionParticipant) {
        jpaRepository.delete(participant)
    }
}
