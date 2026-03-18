package sypg.runtogether.domain.location

import java.time.LocalDateTime

interface LocationLogRepository {
    fun findById(id: Long): LocationLog?
    fun findBySessionId(sessionId: Long): List<LocationLog>
    fun findByUserId(userId: Long): List<LocationLog>
    fun findBySessionIdAndUserId(sessionId: Long, userId: Long): List<LocationLog>
    fun findBySessionIdAndUserIdAfter(sessionId: Long, userId: Long, after: LocalDateTime): List<LocationLog>
    fun save(log: LocationLog): LocationLog
    fun delete(log: LocationLog)
}