package sypg.runtogether.domain.location

import org.springframework.stereotype.Component
import kotlin.math.*

/**
 * 거리 계산 도메인 서비스
 *
 * Haversine 공식을 사용하여 두 GPS 좌표 간의 거리를 계산
 */
@Component
class DistanceCalculator {
    
    companion object {
        private const val EARTH_RADIUS_KM = 6371.0
    }
    
    /**
     * 두 GPS 좌표 간의 거리 계산 (미터 단위)
     * 
     * @param lat1 시작 위도
     * @param lon1 시작 경도
     * @param lat2 종료 위도
     * @param lon2 종료 경도
     * @return 거리 (미터)
     */
    fun calculate(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        // 라디안 변환
        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)
        val deltaLat = Math.toRadians(lat2 - lat1)
        val deltaLon = Math.toRadians(lon2 - lon1)
        
        // Haversine 공식
        val a = sin(deltaLat / 2).pow(2) +
                cos(lat1Rad) * cos(lat2Rad) *
                sin(deltaLon / 2).pow(2)
        
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        // 거리 (킬로미터 → 미터)
        return EARTH_RADIUS_KM * c * 1000
    }
    
    /**
     * LocationLog 두 개로 거리 계산
     */
    fun calculateBetween(from: LocationLog, to: LocationLog): Double {
        return calculate(from.latitude, from.longitude, to.latitude, to.longitude)
    }
}
