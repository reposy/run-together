package sypg.runtogether.infrastructure.persistence.session

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import sypg.runtogether.domain.session.UserSessionStat
import sypg.runtogether.domain.session.UserSessionStatRepository

@Repository
class UserSessionStatRepositoryImpl(
    private val jpaRepository: UserSessionStatJpaRepository
) : UserSessionStatRepository {

    override fun findById(id: Long): UserSessionStat? {
        return jpaRepository.findById(id).orElse(null)
    }

    override fun findBySessionId(sessionId: Long): List<UserSessionStat> {
        return jpaRepository.findBySessionId(sessionId)
    }

    override fun findByUserId(userId: Long): List<UserSessionStat> {
        return jpaRepository.findByUserId(userId)
    }

    override fun findBySessionIdAndUserId(sessionId: Long, userId: Long): UserSessionStat? {
        return jpaRepository.findBySessionIdAndUserId(sessionId, userId)
    }

    override fun findTopBySessionIdOrderByTotalDistanceDesc(sessionId: Long, limit: Int): List<UserSessionStat> {
        return jpaRepository.findBySessionIdOrderByTotalDistanceDesc(
            sessionId,
            PageRequest.of(0, limit)
        )
    }

    override fun save(stat: UserSessionStat): UserSessionStat {
        return jpaRepository.save(stat)
    }

    override fun delete(stat: UserSessionStat) {
        jpaRepository.delete(stat)
    }
}
