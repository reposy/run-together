package sypg.runtogether.domain.session

import java.time.LocalDateTime

class SessionParticipant (
    val id: Long,
    val sessionId: Long,
    val userId: Long,
    val status: ParticipantStatus,
    val joinedAt: LocalDateTime,
) {

}