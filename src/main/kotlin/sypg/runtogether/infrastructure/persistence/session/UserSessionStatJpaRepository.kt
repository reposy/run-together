package sypg.runtogether.infrastructure.persistence.session

import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import sypg.runtogether.domain.session.UserSessionStat

interface UserSessionStatJpaRepository : JpaRepository<UserSessionStat, Long> {
    fun findBySessionId(sessionId: Long): List<UserSessionStat>
    fun findByUserId(userId: Long): List<UserSessionStat>
    fun findBySessionIdAndUserId(sessionId: Long, userId: Long): UserSessionStat?
    fun findBySessionIdOrderByTotalDistanceDesc(sessionId: Long, pageable: PageRequest): List<UserSessionStat>
}
