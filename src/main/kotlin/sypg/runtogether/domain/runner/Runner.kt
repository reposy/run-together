package sypg.runtogether.domain.runner

import java.time.LocalDateTime

class Runner (
    val id: Long,
    val nickname: String,
    val username: String,
    val passwordHash: String,
    val createAt: LocalDateTime,
) {

}