package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.*
import com.example.movie_ticketing.repository.*
import com.example.movie_ticketing.service.MovieService
import com.example.movie_ticketing.service.SeatService
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * 예약 순서 : 영화 선택 -> 날짜 선택 -> 시간 선택 -> 좌석 선택
 */
@Controller
class ReservationController(
    private val theaterRepository: TheaterRepository,
    private val scheduleRepository: ScheduleRepository,
    private val ticketRepository: TicketRepository,
    private val memberRepository: MemberRepository,
    private val reservationRepository: ReservationRepository,
    private val seatRepository: SeatRepository
) {

    @Transactional
    @PostMapping("/user/reservation")
    fun selectReservation(@Valid form: ReservationForm, model: Model, @AuthenticationPrincipal user: User): String {
        println("Received form: $form")

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // form.date의 형식에 맞게 패턴을 설정합니다.
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm") // form.time의 형식에 맞게 패턴을 설정합니다.
        //form에서 전달된 것은 String Type이므로 로컬타임 로컬데이트로 전환
        val date: LocalDate = LocalDate.parse(form.date, dateFormatter)
        val time: LocalTime = LocalTime.parse(form.time, timeFormatter)

        val email = user.username
        val member = memberRepository.findByEmail(email).orElseThrow()

        val reservation = Reservation().apply {
            this.member = member
            this.date = LocalDate.now()
        }
        println(reservation)
        reservationRepository.save(reservation)

        val theater = theaterRepository.findByName(form.theaterName) //관 이름을 전송했기에 관id를 찾아서 저장
        val schedule = scheduleRepository.findByMovieIdAndDateAndStartAndTheaterId(form.movieId, date, time,theater.id)

        form.seatNumbers.forEach{number -> //선택된 좌석들을 받아옴 = 리스트형식이기에 각각의 정보들을 이용해 티켓에 저장
            val seat = seatRepository.findByTheaterAndSeatNumber(schedule.theater,number) //form태그에서 온 것은 seatNumber이기에 theaterid로 좌석을 구분
            val ticket = Ticket().apply {                               // why? seatNumber는 모두 1~49이기에
                this.schedule = schedule                // 구한 좌석을 통해 스케줄과 좌석을 맵핑해서 저장
                this.seat = seat
                this.reservation = reservation
            }
            println(ticket)
            ticketRepository.save(ticket)
        }

        return "redirect:/reservationComplete"
    }

    @PostMapping("/cancelReservation")
    fun cancelReservation(ticketId: Int, result: BindingResult): String {

        if (result.hasErrors()) {
            return "redirect:/reservation"
        }

        val ticket = ticketRepository.findById(ticketId)
            .orElseThrow { IllegalArgumentException("error") }

        ticketRepository.delete(ticket)

        return "redirect:/mypage2"
    }
    @GetMapping("/reservationComplete")
    fun complete(model: Model):String{
        return "reservationComplete"
    }
}