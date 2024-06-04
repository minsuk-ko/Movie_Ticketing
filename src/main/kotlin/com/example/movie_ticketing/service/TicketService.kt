package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Ticket
import com.example.movie_ticketing.repository.TicketRepository
import org.springframework.stereotype.Service
@Service
class TicketService (private val ticketRepository: TicketRepository){
    fun getTicketsByTheaterId(theaterId: Int): List<Ticket> {
        return ticketRepository.findByScheduleTheaterId(theaterId)
    }

}