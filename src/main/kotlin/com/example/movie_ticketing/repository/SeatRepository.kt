package com.example.movie_ticketing.repository


import com.example.movie_ticketing.domain.Seat
import com.example.movie_ticketing.domain.Theater
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SeatRepository  : JpaRepository<Seat, Int> {
    fun findByTheaterAndSeatNumber(theater:Theater,seatNumber: Int):Seat
}