package sypg.runtogether.application.session

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sypg.runtogether.domain.session.RunningSession
import sypg.runtogether.domain.session.RunningSessionRepository
import java.time.LocalDateTime

/**
 * 러닝 세션 생성 UseCase
 * 
 * 사용처:
 * - 시스템 배치: 매일 정해진 시간에 자동 생성
 * - 관리자: 수동으로 특정 시간에 세션 생성
 */
@Service
class CreateSessionUseCase(
    private val runningSessionRepository: RunningSessionRepository
) {
    /**
     * 러닝 세션 생성
     * 
     * @param startAt 세션 시작 시간
     * @param endAt 세션 종료 시간
     * @param createdBy 생성자 ID (null이면 시스템, 값 있으면 관리자 ID)
     * @return 생성된 세션 ID
     */
    @Transactional
    fun execute(
        startAt: LocalDateTime,
        endAt: LocalDateTime,
        createdBy: Long? = null
    ): Long {
        // 시간 검증
        require(startAt.isAfter(LocalDateTime.now())) { 
            "Start time must be in the future" 
        }
        require(endAt.isAfter(startAt)) { 
            "End time must be after start time" 
        }

        // 세션 생성
        val session = RunningSession.create(
            startAt = startAt,
            endAt = endAt
        )

        // 저장
        val saved = runningSessionRepository.save(session)

        return saved.id
    }
}
