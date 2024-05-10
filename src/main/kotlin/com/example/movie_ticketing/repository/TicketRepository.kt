package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Reservation
import com.example.movie_ticketing.domain.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<Ticket, Int> {
    fun deleteByReservation(reservation: Reservation){
    }
}