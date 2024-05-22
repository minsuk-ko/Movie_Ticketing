package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.*
import com.example.movie_ticketing.repository.MemberRepository
import com.example.movie_ticketing.repository.ReservationRepository
import com.example.movie_ticketing.repository.ScheduleRepository
import com.example.movie_ticketing.repository.TicketRepository
import com.example.movie_ticketing.service.MovieService
import com.example.movie_ticketing.service.ReservationService
import com.example.movie_ticketing.service.SeatService
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 예약 순서 : 영화 선택 -> 날짜 선택 -> 시간 선택 -> 좌석 선택
 */
@Controller
class ReservationController(
    private val movieService: MovieService,
    private val reservationService: ReservationService,
    private val scheduleRepository: ScheduleRepository,
    private val ticketRepository: TicketRepository,
    private val memberRepository: MemberRepository,
    private val reservationRepository: ReservationRepository,
    private val seatService: SeatService // 추가된 seatService
) {

    @Transactional
    @PostMapping("/selectReservation")
    fun selectReservation(@Valid form: ReservationForm, result: BindingResult, model: Model, @AuthenticationPrincipal user: User): String {
        val email = user.username
        val member = memberRepository.findByEmail(email).orElseThrow()

        if (result.hasErrors()) {
            return "redirect:/reservation"
        }

        val schedule = scheduleRepository.findByMovieIdAndDateAndStart(form.movieId, form.date, form.time)

        val selectedSeats = seatService.findSeatsByIds(form.seatIds)

        val reservation = Reservation().apply {
            this.member = member
            this.date = LocalDate.now().toString()
        }
        reservationRepository.save(reservation)

        selectedSeats.forEach { seat ->
            seat.isSelected = true
            seatService.save(seat)
        }

        selectedSeats.forEach { seat ->
            val ticket = Ticket().apply {
                this.schedule = schedule
                this.seat = seat
                this.reservation = reservation
            }
            reservationService.saveTicket(ticket)
        }

        print("예매 완료!")
        return "redirect:/reservationComplete"
    }

    @PostMapping("/cancelReservation")
    fun cancelReservation(ticketId: Int, result: BindingResult): String {

        if (result.hasErrors()) {
            return "redirect:/reservation"
        }

        val ticket = ticketRepository.findById(ticketId)
            .orElseThrow { IllegalArgumentException("error") }

        ticket.seat.isSelected = false
        seatService.save(ticket.seat)

        ticketRepository.delete(ticket)

        return "redirect:/mypage2"
    }

    fun generateDateRange(): List<String> {
        return (0 until 10).map { LocalDate.now().plusDays(it.toLong()).format(DateTimeFormatter.ISO_DATE) }
    }

    fun generateTimeSlots(): List<String> {
        return listOf("10:00", "12:30", "15:00", "17:30", "20:00", "22:30")
    }
}