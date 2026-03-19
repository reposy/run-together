package sypg.runtogether.domain.location

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * 거리 계산 로직 검증 테스트
 *
 * Haversine 공식이 정확하게 작동하는지 확인
 */
class DistanceCalculatorTest {

    private val calculator = DistanceCalculator()

    @Test
    fun `서울 시청에서 광화문까지 거리 계산 - 약 600m`() {
        // 서울 시청: 37.5665, 126.9780
        // 광화문: 37.5720, 126.9769
        val distance = calculator.calculate(37.5665, 126.9780, 37.5720, 126.9769)

        // 실제 거리는 약 610m
        assertTrue(distance > 550 && distance < 650,
            "Expected ~600m, but got ${distance}m")

        println("서울 시청 → 광화문: ${distance}m")
    }

    @Test
    fun `같은 위치의 거리는 0m`() {
        val distance = calculator.calculate(37.5665, 126.9780, 37.5665, 126.9780)

        assertEquals(0.0, distance, 0.1)
    }

    @Test
    fun `위도 0_01도 이동 - 약 1_11km`() {
        // 적도에서 위도 0.01도 이동 (약 1.11km)
        val distance = calculator.calculate(0.0, 0.0, 0.01, 0.0)

        assertTrue(distance > 1000 && distance < 1200,
            "Expected ~1100m, but got ${distance}m")

        println("위도 0.01도 이동: ${distance}m")
    }

    @Test
    fun `LocationLog 객체를 이용한 거리 계산`() {
        val log1 = LocationLog.record(
            userId = 1,
            sessionId = 1,
            latitude = 37.5665,
            longitude = 126.9780
        )

        val log2 = LocationLog.record(
            userId = 1,
            sessionId = 1,
            latitude = 37.5720,
            longitude = 126.9769
        )

        val distance = calculator.calculateBetween(log1, log2)

        // 서울 시청 → 광화문 (약 600m)
        assertTrue(distance > 550 && distance < 650,
            "Expected ~600m, but got ${distance}m")

        println("LocationLog 기반 계산: ${distance}m")
    }

    @Test
    fun `북쪽으로 이동한 거리와 남쪽으로 이동한 거리는 동일`() {
        val distanceNorth = calculator.calculate(37.5, 127.0, 37.6, 127.0)
        val distanceSouth = calculator.calculate(37.5, 127.0, 37.4, 127.0)

        assertEquals(distanceNorth, distanceSouth, 1.0)
    }
}
