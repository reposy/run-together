package sypg.runtogether.domain.session

interface UserSessionStatRepository {
    fun findById(id: Long): UserSessionStat?
    fun findBySessionId(sessionId: Long): List<UserSessionStat>
    fun findByUserId(userId: Long): List<UserSessionStat>
    fun findBySessionIdAndUserId(sessionId: Long, userId: Long): UserSessionStat?
    fun findTopBySessionIdOrderByTotalDistanceDesc(sessionId: Long, limit: Int): List<UserSessionStat>
    fun save(stat: UserSessionStat): UserSessionStat
    fun delete(stat: UserSessionStat)
}