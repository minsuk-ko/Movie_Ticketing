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

    // 전체 좌석 가져오기
    fun getAllSeats(theater: Theater): List<Seat> {
        return seatRepository.findByTheater(theater)
    }

    @Transactional
    fun selectSeat(seat : Seat) : Optional<Seat> {
        val seatNum = seatRepository.findById(seat.id)

        seatRepository.save(seatNum)

        return seatNum
    }
}