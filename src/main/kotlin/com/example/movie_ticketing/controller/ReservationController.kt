package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.*
import com.example.movie_ticketing.repository.MemberRepository
import com.example.movie_ticketing.repository.MovieRepository
import com.example.movie_ticketing.service.MovieService
import com.example.movie_ticketing.service.ReservationService
import com.example.movie_ticketing.service.SeatService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 예약 순서 : 영화 선택 -> 날짜 선택 -> 시간 선택 ==> 좌석 선택
 */
@Controller
class ReservationController(
    private val movieService: MovieService,
    private val seatService: SeatService,
    private val reservationService: ReservationService) {

    /**
     * 영화, 날짜, 시간 선택
     */
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
    // 물어볼때는 ,,, 뭐가 문제여서 내가 이걸 여기까지 했는데 이부분이 뭔지 모르겠따.
    // 티켓을 어떻게 처리할건지가 엮여있어서 그럼
    // !! selectMovieAndDateAndTime, getAllSeats, selectSeat 다시 봐야됨
    @PostMapping("/selectReservation")
    fun selectReservation(member: Member, movie: Movie, reservation: Reservation, model : Model) : String {
        try {
           // val reserve = reservationService.selectMovieAndDateAndTime(movie, reservation)
//
//            model.addAttribute("selectMovieAndDateAndTime", reserve)
            return "GoToSeatSelectionPage"
        } catch (e: Exception) {
            model.addAttribute("errorMessage", "예약 과정에서 에러가 발생했습니다.")
            return "reservationForm"
        }
    }

    @GetMapping("/reservation/seats")
    fun chooseSeats(theater : Theater, model : Model) : String {
        val availableSeats = seatService.getAllSeats(theater)

        model.addAttribute("seats",availableSeats)

        return "seats"  // 좌석 선택 페이지 이동
    }

    @PostMapping("/selectSeat")
    fun selectSeat(seat : Seat, model : Model) : String {
        val selectSeat = seatService.selectSeat(seat)
        model.addAttribute("selectSeat", selectSeat)
        return "SuccessReservation"
    }

    // (임시) 날짜의 범위를 현재부터 10일 후까지로 설정
    private fun generateDateRange(): List<String> {
        return (0 until 10).map { LocalDate.now().plusDays(it.toLong()).format(DateTimeFormatter.ISO_DATE) }
    }

    // 시간대 예시
    // 추후 api 에서 상영시간에 대한 정보를 찾거나 시간대를 바꿀 수 있으면 바꿀 것
    private fun generateTimeSlots(): List<String> {
        return listOf("10:00", "12:30", "15:00", "17:30", "20:00", "22:30")
    }
}
