package sypg.runtogether.application.session

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sypg.runtogether.domain.session.*
import java.time.LocalDateTime

/**
 * 세션 참가 UseCase
 * 
 * 사용자가 러닝 세션에 참가
 */
@Service
class JoinSessionUseCase(
    private val runningSessionRepository: RunningSessionRepository,
    private val sessionParticipantRepository: SessionParticipantRepository,
    private val userSessionStatRepository: UserSessionStatRepository
) {
    /**
     * 세션에 참가
     * 
     * @param sessionId 참가할 세션 ID
     * @param userId 참가하는 사용자 ID
     * @return 생성된 참가자 ID
     */
    @Transactional
    fun execute(sessionId: Long, userId: Long): Long {
        // 세션 존재 확인
        val session = runningSessionRepository.findById(sessionId)
            ?: throw IllegalArgumentException("Session not found: $sessionId")

        // 세션이 참가 가능한 상태인지 확인
        require(session.status == SessionStatus.READY || session.status == SessionStatus.RUNNING) {
            "Session is not available for joining: ${session.status}"
        }

        // 세션 시간 확인 (아직 종료되지 않았는지)
        require(session.endAt.isAfter(LocalDateTime.now())) {
            "Session has already ended"
        }

        // 이미 참가했는지 확인
        require(!sessionParticipantRepository.existsBySessionIdAndUserId(sessionId, userId)) {
            "User already joined this session"
        }

        // 참가자 생성
        val participant = SessionParticipant.join(
            sessionId = sessionId,
            userId = userId
        )
        val savedParticipant = sessionParticipantRepository.save(participant)

        // 통계 초기화
        val stat = UserSessionStat.create(
            sessionId = sessionId,
            userId = userId
        )
        userSessionStatRepository.save(stat)

        return savedParticipant.id
    }
}
