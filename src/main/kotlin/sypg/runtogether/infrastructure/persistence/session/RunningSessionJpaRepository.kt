package sypg.runtogether.infrastructure.persistence.session

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import sypg.runtogether.domain.session.RunningSession
import sypg.runtogether.domain.session.SessionStatus
import java.time.LocalDateTime

interface RunningSessionJpaRepository : JpaRepository<RunningSession, Long> {
    fun findByStatus(status: SessionStatus): List<RunningSession>
    
    @Query("SELECT s FROM RunningSession s WHERE s.startAt <= :time AND s.endAt >= :time")
    fun findActiveSessionsAt(@Param("time") time: LocalDateTime): List<RunningSession>
}
