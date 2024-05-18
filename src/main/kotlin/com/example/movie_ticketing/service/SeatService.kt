package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.*
import com.example.movie_ticketing.repository.SeatRepository
import org.springframework.stereotype.Service

@Service
class SeatService(private val seatRepository: SeatRepository) {

    fun save(seat: Seat): Seat {
        return seatRepository.save(seat)
    }

    fun findSeatsByIds(seatIds: List<Int>): List<Seat> { // List<Long>을 List<Int>로 변경
        return seatRepository.findAllById(seatIds.map { it.toLong() })
    }

    fun getAllSeats(theater: String): List<Seat> {
        // 예시: 특정 극장의 모든 좌석을 반환합니다. 필요에 따라 구현할 수 있습니다.
        return seatRepository.findAll()
    }

    fun selectSeat(seat: Seat): Seat {
        seat.isSelected = true
        return seatRepository.save(seat)
    }
}
