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
     * READY 또는 RUNNING 상태의 모든 세션을 반환합니다.
     *
     * @return 활성 세션 목록과 각 세션의 참가자 수 (빈 리스트 가능)
     */
    @Transactional(readOnly = true)
    fun execute(): List<ActiveSessionResult> {
        // READY 상태 세션 조회
        val readySessions = runningSessionRepository.findByStatus(SessionStatus.READY)

        // RUNNING 상태 세션 조회
        val runningSessions = runningSessionRepository.findByStatus(SessionStatus.RUNNING)

        // 모든 활성 세션 합치기
        val activeSessions = readySessions + runningSessions

        if (activeSessions.isEmpty()) {
            return emptyList()
        }

        // 각 세션에 대해 참가자 수를 조회하여 결과 생성
        return activeSessions
            .sortedByDescending { it.startAt }  // 최신순 정렬
            .map { session ->
                val participants = sessionParticipantRepository.findBySessionId(session.id)
                ActiveSessionResult(
                    session = session,
                    participantCount = participants.size
                )
            }
    }

    /**
     * 활성 세션 결과
     */
    data class ActiveSessionResult(
        val session: RunningSession,
        val participantCount: Int
    )
}
