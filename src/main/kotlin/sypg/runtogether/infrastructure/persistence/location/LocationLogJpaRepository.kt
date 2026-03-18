package sypg.runtogether.infrastructure.persistence.location

import org.springframework.data.jpa.repository.JpaRepository
import sypg.runtogether.domain.location.LocationLog
import java.time.LocalDateTime

interface LocationLogJpaRepository : JpaRepository<LocationLog, Long> {
    fun findBySessionId(sessionId: Long): List<LocationLog>
    fun findByUserId(userId: Long): List<LocationLog>
    fun findBySessionIdAndUserId(sessionId: Long, userId: Long): List<LocationLog>
    fun findBySessionIdAndUserIdAndRecordedAtAfter(
        sessionId: Long,
        userId: Long,
        after: LocalDateTime
    ): List<LocationLog>
}
