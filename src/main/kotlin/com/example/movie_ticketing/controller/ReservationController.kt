package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.*
import com.example.movie_ticketing.repository.ScheduleRepository
import com.example.movie_ticketing.service.MovieService
import com.example.movie_ticketing.service.ReservationService
import com.example.movie_ticketing.service.SeatService
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
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
    private val seatService: SeatService,
    private val reservationService: ReservationService,
    private val scheduleRepository: ScheduleRepository) {

    @GetMapping("/reservation")
    fun createForm(model : Model): String {
        val titles = movieService.findAvailableMovies()
        val dates = generateDateRange()
        val times = generateTimeSlots()

        model.addAttribute("titles", titles)
        model.addAttribute("dates", dates)
        model.addAttribute("times", times)

        return "reservationForm" // 예약 페이지 이동
    }

    /**
     * 영화, 날짜, 시간, 좌석 선택이 한 페이지에 구성되어 있음
     */
    @PostMapping("/selectReservation")
    fun selectReservation(@Valid form : ReservationForm, result: BindingResult , model : Model) : String {

        println(form)

        if (result.hasErrors()) {
            return "redirect:/reservation";
        }
        println(form)

        // 영화, 날짜, 시간을 선택
        val schedule = scheduleRepository.findByMovieIdAndDateAndStart(form.movieId, form.date, form.time)

        // 선택한 좌석 정보를 가져옴
        val selectedSeats = seatService.findSeatsByIds(form.seatIds)

        // 선택한 좌석들을 예약된 상태로 변경
        // 선택된 좌석은 추후 예약할 때 예약할 수 없어야 함.
        selectedSeats.forEach { seat ->
            seat.isSelected = true
            seatService.save(seat) // isSelected 상태를 업데이트하기 위해 저장
        }

        // 여러 좌석을 선택할 수 있으므로, 각 좌석마다 별도의 티켓을 생성
        selectedSeats.forEach { seat ->
            val ticket = Ticket().apply {
                this.schedule = schedule // 스케줄 설정
                this.seat = seat // 현재 좌석 설정
                // 영화에 대한 다른 정보가 티켓에 필요하다면 여기에 추가하면 된다.
            }
            // 생성된 티켓을 저장
            reservationService.saveTicket(ticket)
        }

        return "redirect:/reservationComplete"
    }

    /**
     *  영화, 날짜, 시간, 좌석이 한 페이지에 구성되어 있기 때문에 주석처리
     */
//    @GetMapping("/reservation/seats")
//    fun chooseSeats(theater : Theater, model : Model) : String {
//        val availableSeats = seatService.getAllSeats(theater)
//
//        model.addAttribute("seats",availableSeats)
//
//        return "seats"  // 좌석 선택 페이지 이동
//    }
//
//    @PostMapping("/selectSeat")
//    fun selectSeat(seat : Seat, model : Model) : String {
//        val selectSeat = seatService.selectSeat(seat)
//        model.addAttribute("selectSeat", selectSeat)
//        return "SuccessReservation"
//    }

    // (임시) 날짜의 범위를 현재부터 10일 후까지로 설정
    fun generateDateRange(): List<String> {
        return (0 until 10).map { LocalDate.now().plusDays(it.toLong()).format(DateTimeFormatter.ISO_DATE) }
    }

    // 시간대 예시
    // 추후 api 에서 상영시간에 대한 정보를 찾거나 시간대를 바꿀 수 있으면 바꿀 것
    fun generateTimeSlots(): List<String> {
        return listOf("10:00", "12:30", "15:00", "17:30", "20:00", "22:30")
    }
}
