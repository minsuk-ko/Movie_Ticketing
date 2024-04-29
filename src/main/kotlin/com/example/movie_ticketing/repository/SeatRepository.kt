package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Seat
import com.example.movie_ticketing.domain.Theater
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SeatRepository : JpaRepository<Seat, Int> {

    fun findByTheater(theater : Theater): List<Seat>
    fun save(seatNum: Optional<Seat>)
}