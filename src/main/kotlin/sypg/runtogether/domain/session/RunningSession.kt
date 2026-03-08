package sypg.runtogether.domain.session

import java.time.LocalDateTime

class RunningSession (
    val id: Long,
    val status: SessionStatus,
    val startAt: LocalDateTime,
    val createAt: LocalDateTime,
) {

}