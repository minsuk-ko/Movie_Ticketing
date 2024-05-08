package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.domain.Reservation
import com.example.movie_ticketing.domain.Seat
import com.example.movie_ticketing.domain.Theater
import com.example.movie_ticketing.repository.SeatRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SeatService(private val seatRepository: SeatRepository) {

    /**
     * 주어진 좌석 ID 목록에 해당하는 Seat 엔티티들을 조회하여 반환
     * 예약할 때 선택한 좌석 정보를 가져옴
     */
    fun findSeatsByIds(seatIds: List<Int>): List<Seat> {
        return seatRepository.findByIdIn(seatIds)
    }

    /**
     *  나중에 비즈니스 로직이 변경되거나 추가될 때 해당 로직을 Service 에서 관리할 수 있음.(유지보수 ↑)
     */
    fun save(seat: Seat): Seat {
        return seatRepository.save(seat)
    }

}