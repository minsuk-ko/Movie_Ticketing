package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Reservation
import com.example.movie_ticketing.domain.Schedule
import com.example.movie_ticketing.domain.Seat
import com.example.movie_ticketing.domain.Ticket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : JpaRepository<Ticket, Int> {
    fun deleteByReservation(reservation: Reservation){
    }

    //단일 reservation을 통해서 티켓을가져옴
    //where reservation_id =?
    //한 예약에 대한 모든 티켓 -> 나중에 사용할 수 있을 듯?
    fun findByReservation(reservation: Reservation):List<Ticket>
    fun findByScheduleAndSeat(schedule: Schedule,seat:Seat):List<Ticket>
    fun findBySchedule(schedule: Schedule):List<Ticket>
    //reservation객체들의 리스트
    //where reservation_id IN (?,?,?.....)형식
    //각각의 예약에 따른 티켓들을 모두 가져올 수있음
    fun findByReservationIn(reservations:List<Reservation>):List<Ticket>
    fun findByReservationId(reservationId:Int):List<Ticket>
    fun findByTheaterId(id:Int):List<Ticket>
    fun findByScheduleTheaterId(theaterId: Int): List<Ticket>

}