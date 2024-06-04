package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Reservation
import com.example.movie_ticketing.domain.Schedule
import com.example.movie_ticketing.domain.Seat
import com.example.movie_ticketing.domain.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<Ticket, Int> {
    @Query("SELECT t FROM Ticket t WHERE t.schedule.theater.id = :theaterId")
    fun findByScheduleTheaterId(theaterId: Int): List<Ticket>

    fun deleteByReservation(reservation: Reservation)


    //단일 reservation을 통해서 티켓을가져옴
    //where reservation_id =?
    //한 예약에 대한 모든 티켓 -> 나중에 사용할 수 있을 듯?
    fun findByReservation(reservation: Reservation):List<Ticket>

    fun findBySchedule(schedule: Schedule):List<Ticket>

    fun findByReservationId(reservationId: Int): List<Ticket>


}