package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Reservation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*
@Repository
interface ReservationRepository : JpaRepository<Reservation, Int> {
    fun save(reservation : Optional<Reservation>)

    fun findByMemberId(memberid : Int):List<Reservation>
}