package sypg.runtogether.domain.session

import java.time.LocalDateTime

class SessionParticipant (
    val id: Long,
    val sessionId: Long,
    val runnerId: Long,
    val status: ParticipantStatus,
    val joinedAt: LocalDateTime,
) {

}