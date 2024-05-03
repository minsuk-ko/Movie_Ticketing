package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.*
import com.example.movie_ticketing.repository.*
import org.hibernate.id.enhanced.Optimizer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Optional

@Service
class ReservationService(private val ticketRepository: TicketRepository) {

    /**
     *  나중에 비즈니스 로직이 변경되거나 추가될 때 해당 로직을 Service 에서 관리할 수 있음.(유지보수 ↑)
     */
    fun saveTicket(ticket: Ticket): Ticket {
        return ticketRepository.save(ticket)
    }
}