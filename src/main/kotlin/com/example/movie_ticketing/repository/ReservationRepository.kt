package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.*

interface ReservationRepository : JpaRepository<Reservation, Int> {
    fun save(reservation : Optional<Reservation>)
}