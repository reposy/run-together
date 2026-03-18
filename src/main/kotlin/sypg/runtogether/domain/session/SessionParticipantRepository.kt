package sypg.runtogether.domain.session

interface SessionParticipantRepository {
    fun findById(id: Long): SessionParticipant?
    fun findBySessionId(sessionId: Long): List<SessionParticipant>
    fun findByUserId(userId: Long): List<SessionParticipant>
    fun findBySessionIdAndUserId(sessionId: Long, userId: Long): SessionParticipant?
    fun existsBySessionIdAndUserId(sessionId: Long, userId: Long): Boolean
    fun save(participant: SessionParticipant): SessionParticipant
    fun delete(participant: SessionParticipant)
}