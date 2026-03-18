package sypg.runtogether.infrastructure.persistence.session

import org.springframework.data.jpa.repository.JpaRepository
import sypg.runtogether.domain.session.SessionParticipant

interface SessionParticipantJpaRepository : JpaRepository<SessionParticipant, Long> {
    fun findBySessionId(sessionId: Long): List<SessionParticipant>
    fun findByUserId(userId: Long): List<SessionParticipant>
    fun findBySessionIdAndUserId(sessionId: Long, userId: Long): SessionParticipant?
    fun existsBySessionIdAndUserId(sessionId: Long, userId: Long): Boolean
}
