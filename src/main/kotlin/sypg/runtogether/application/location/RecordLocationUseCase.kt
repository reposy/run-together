package sypg.runtogether.application.location

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sypg.runtogether.domain.location.DistanceCalculator
import sypg.runtogether.domain.location.LocationLog
import sypg.runtogether.domain.location.LocationLogRepository
import sypg.runtogether.domain.session.ParticipantStatus
import sypg.runtogether.domain.session.SessionParticipantRepository
import sypg.runtogether.domain.session.UserSessionStatRepository

/**
 * 위치 기록 UseCase
 * 
 * 사용자의 GPS 위치를 기록하고 거리/통계를 업데이트
 */
@Service
class RecordLocationUseCase(
    private val locationLogRepository: LocationLogRepository,
    private val sessionParticipantRepository: SessionParticipantRepository,
    private val userSessionStatRepository: UserSessionStatRepository,
    private val distanceCalculator: DistanceCalculator
) {
    /**
     * 위치 기록 및 통계 업데이트
     * 
     * @param sessionId 세션 ID
     * @param userId 사용자 ID
     * @param latitude 위도
     * @param longitude 경도
     * @return 생성된 LocationLog ID
     */
    @Transactional
    fun execute(
        sessionId: Long,
        userId: Long,
        latitude: Double,
        longitude: Double
    ): Long {
        // 참가자 확인 (RUNNING 상태인지)
        val participant = sessionParticipantRepository.findBySessionIdAndUserId(sessionId, userId)
            ?: throw IllegalArgumentException("Participant not found")
        
        require(participant.status == ParticipantStatus.RUNNING) {
            "Participant must be RUNNING to record location"
        }

        // 위치 기록
        val locationLog = LocationLog.record(
            userId = userId,
            sessionId = sessionId,
            latitude = latitude,
            longitude = longitude
        )
        val savedLog = locationLogRepository.save(locationLog)

        // 이전 위치 조회
        val previousLogs = locationLogRepository.findBySessionIdAndUserId(sessionId, userId)
        if (previousLogs.size >= 2) {
            // 최근 두 개의 위치로 거리 계산
            val sortedLogs = previousLogs.sortedByDescending { it.recordedAt }
            val current = sortedLogs[0]
            val previous = sortedLogs[1]
            
            val distance = distanceCalculator.calculateBetween(previous, current)
            
            // 통계 업데이트
            val stat = userSessionStatRepository.findBySessionIdAndUserId(sessionId, userId)
                ?: throw IllegalStateException("UserSessionStat not found")
            
            // 누적 거리 계산
            val newTotalDistance = stat.totalDistance + distance
            
            // 소요 시간 계산 (초 단위)
            val duration = java.time.Duration.between(
                previousLogs.minOf { it.recordedAt },
                current.recordedAt
            ).seconds
            
            stat.updateStats(newTotalDistance, duration)
            userSessionStatRepository.save(stat)
        }

        return savedLog.id
    }
}
