package sypg.runtogether.application.session

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sypg.runtogether.domain.session.RunningSession
import sypg.runtogether.domain.session.RunningSessionRepository
import sypg.runtogether.domain.session.SessionParticipantRepository
import sypg.runtogether.domain.session.SessionStatus

/**
 * 현재 활성 세션 조회 UseCase
 *
 * READY 또는 RUNNING 상태의 세션을 조회하여 반환합니다.
 */
@Service
class GetActiveSessionUseCase(
    private val runningSessionRepository: RunningSessionRepository,
    private val sessionParticipantRepository: SessionParticipantRepository
) {
    /**
     * 현재 활성 세션 조회
     *
     * READY 또는 RUNNING 상태의 세션 중 가장 최근 세션을 반환합니다.
     *
     * @return 활성 세션과 참가자 수 (세션이 없으면 null)
     */
    @Transactional(readOnly = true)
    fun execute(): ActiveSessionResult? {
        // READY 상태 세션 조회
        val readySessions = runningSessionRepository.findByStatus(SessionStatus.READY)

        // RUNNING 상태 세션 조회
        val runningSessions = runningSessionRepository.findByStatus(SessionStatus.RUNNING)

        // 모든 활성 세션 합치기
        val activeSessions = readySessions + runningSessions

        if (activeSessions.isEmpty()) {
            return null
        }

        // 가장 최근 세션 (startAt이 가장 늦은 세션)
        val latestSession = activeSessions.maxByOrNull { it.startAt }
            ?: return null

        // 참가자 수 조회
        val participants = sessionParticipantRepository.findBySessionId(latestSession.id)

        return ActiveSessionResult(
            session = latestSession,
            participantCount = participants.size
        )
    }

    /**
     * 활성 세션 결과
     */
    data class ActiveSessionResult(
        val session: RunningSession,
        val participantCount: Int
    )
}
