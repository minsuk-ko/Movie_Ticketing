package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.*
import com.example.movie_ticketing.repository.SeatRepository
import org.springframework.stereotype.Service

@Service
class SeatService(private val seatRepository: SeatRepository) {

    fun save(seat: Seat): Seat {
        return seatRepository.save(seat)
    }
}
