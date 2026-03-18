package sypg.runtogether.application.session

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sypg.runtogether.domain.session.SessionParticipantRepository

/**
 * 러닝 시작 UseCase
 * 
 * 참가자가 실제로 러닝을 시작
 */
@Service
class StartRunningUseCase(
    private val sessionParticipantRepository: SessionParticipantRepository
) {
    /**
     * 러닝 시작
     * 
     * @param sessionId 세션 ID
     * @param userId 사용자 ID
     */
    @Transactional
    fun execute(sessionId: Long, userId: Long) {
        // 참가자 확인
        val participant = sessionParticipantRepository.findBySessionIdAndUserId(sessionId, userId)
            ?: throw IllegalArgumentException("Participant not found for session: $sessionId, user: $userId")

        // 러닝 시작
        participant.startRunning()

        // 저장 (상태 변경)
        sessionParticipantRepository.save(participant)
    }
}
