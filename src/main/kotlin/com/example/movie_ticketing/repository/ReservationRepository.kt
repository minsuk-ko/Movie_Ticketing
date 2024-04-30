package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Reservation
import com.example.movie_ticketing.domain.Seat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReservationRepository : JpaRepository<Reservation, Int> {
    fun save(reservation : Optional<Reservation>)
}