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
import org.hibernate.annotations.CreationTimestamp
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 예약 순서 : 영화 선택 -> 날짜 선택 -> 시간 선택 -> 좌석 선택
 */
@Controller
class ReservationController(
    private val movieService: MovieService,
    private val seatService: SeatService,
    private val reservationService: ReservationService,
    private val scheduleRepository: ScheduleRepository,
    private val ticketRepository: TicketRepository,
    private val memberRepository: MemberRepository,
    private val reservationRepository: ReservationRepository) {

//    @GetMapping("/reservation")
//    fun createForm(model : Model): String {
//        val titles = movieService.findAvailableMovies()
//        val dates = generateDateRange()
//        val times = generateTimeSlots()
//
//        model.addAttribute("titles", titles)
//        model.addAttribute("dates", dates)
//        model.addAttribute("times", times)
//
//        return "reservationForm" // 예약 페이지 이동
//    }

    /**
     * 영화, 날짜, 시간, 좌석 선택이 한 페이지에 구성되어 있음
     */
    @Transactional // 티켓과 예약 테이블 둘 다 사용하니까 오류 안나게 방지
    @PostMapping("/selectReservation")
    fun selectReservation(@Valid form : ReservationForm, result: BindingResult , model : Model,@AuthenticationPrincipal user:User) : String {
        val email = user. username  //교수님 sprinsecurity에서 바로 유저만 꺼내옴
        // memberController에서의 mypage1쪽 처럼 Authentication객체를 꺼내서 하나하나 가져와도 되지만 좀 더 간편한 방법
        val member = memberRepository.findByEmail(email).orElseThrow() //optional타입이라 무조건 검증
       // 로그인 한 유저정보를 꺼내서 그 유저의 정보를 member에 넣음
       // 이미 로그인 한 사람의 정보니까 인증되어있음 = .orElseThrow()에 따로 설정필요x

        println(form)

        if (result.hasErrors()) {
            return "redirect:/reservation";
        }
        println(form)

        // 영화, 날짜, 시간을 선택
        val schedule = scheduleRepository.findByMovieIdAndDateAndStart(form.movieId, form.date, form.time)

        // 선택한 좌석 정보를 가져옴
        val selectedSeats = seatService.findSeatsByIds(form.seatIds)

        val reservation = Reservation().apply{
            this.member = member
            //this.date => @CreationTimeStamp로 자동생성
            //이 객체가 생성됐을 때 id필드가 설정되지 않은 상태 0 or null

        }
        reservationRepository.save(reservation)
        //reservation이 db에 저장되어야 id가 자동으로 생성됨
        // 이 엔티티를 저장할 때 데이터 베이스가 자동으로 id 생성시키고 reservation객체의 id필드에 들어감
        //@GeneratedValue(strategy = GenerationType.IDENTITY) 이 어노테이션떄문에 자동으로 생성



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
                this.reservation=reservation  //아까 저장한 객체를 this.reservation에 다 저장
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

    // 마이페이지 - 예약 내역 조회에서의 예약 취소
    @PostMapping("/cancelReservation")
    fun cancelReservation(ticketId: Int, result: BindingResult) : String {

        if (result.hasErrors()) {
            return "redirect:/reservation";
        }

        // 티켓 ID로 티켓 정보 조회
        val ticket = ticketRepository.findById(ticketId)
            .orElseThrow { IllegalArgumentException("error") }

        // 선택한 좌석의 상태를 '선택 가능'으로 변경
        // 티켓 하나에 좌석 하나가 매핑되어 있음을 유의
        ticket.seat.isSelected = true

        // 예약 티켓 삭제
        ticketRepository.delete(ticket)

        // 마이페이지로 이동
        return "redirect:/mypage2"
    }

    // (임시) 날짜의 범위를 현재부터 10일 후까지로 설정
    fun generateDateRange(): List<String> {
        return (0 until 10).map { LocalDate.now().plusDays(it.toLong()).format(DateTimeFormatter.ISO_DATE) }
    }

    // 시간대 예시
    fun generateTimeSlots(): List<String> {
        return listOf("10:00", "12:30", "15:00", "17:30", "20:00", "22:30")
    }
}