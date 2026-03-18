package sypg.runtogether.infrastructure.persistence.location

import org.springframework.stereotype.Repository
import sypg.runtogether.domain.location.LocationLog
import sypg.runtogether.domain.location.LocationLogRepository
import java.time.LocalDateTime

@Repository
class LocationLogRepositoryImpl(
    private val jpaRepository: LocationLogJpaRepository
) : LocationLogRepository {

    override fun findById(id: Long): LocationLog? {
        return jpaRepository.findById(id).orElse(null)
    }

    override fun findBySessionId(sessionId: Long): List<LocationLog> {
        return jpaRepository.findBySessionId(sessionId)
    }

    override fun findByUserId(userId: Long): List<LocationLog> {
        return jpaRepository.findByUserId(userId)
    }

    override fun findBySessionIdAndUserId(sessionId: Long, userId: Long): List<LocationLog> {
        return jpaRepository.findBySessionIdAndUserId(sessionId, userId)
    }

    override fun findBySessionIdAndUserIdAfter(
        sessionId: Long,
        userId: Long,
        after: LocalDateTime
    ): List<LocationLog> {
        return jpaRepository.findBySessionIdAndUserIdAndRecordedAtAfter(sessionId, userId, after)
    }

    override fun save(log: LocationLog): LocationLog {
        return jpaRepository.save(log)
    }

    override fun delete(log: LocationLog) {
        jpaRepository.delete(log)
    }
}
