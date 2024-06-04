package com.example.movie_ticketing.controller


import com.example.movie_ticketing.repository.*
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Controller
class ScheduleController( private val scheduleRepository: ScheduleRepository,
                        private val ticketRepository: TicketRepository) {


@GetMapping("/reservation")  //영화 db가 있어야해서 일단 영화부터 받음
fun viewReservation(model: Model):String{

    val currentDate = LocalDate.now()
    val endDate = currentDate.plusDays(30)
    val schedules =  scheduleRepository.findByDateBetween(currentDate,endDate)// 한달치 계속 꺼냄
    // 영화별로 중복을 제거한 리스트 생성
    val uniqueMovies = schedules.map { it.movie }.distinctBy { it.id } //이래야지 리스트에는 상영하는 영화만 보임

    // 각 스케줄에 대한 예약된 좌석 정보를 맵으로 저장
    val occupiedSeatsMap = schedules.associateBy(
        { it.id },
        { schedule ->
            ticketRepository.findBySchedule(schedule).map { it.seat.seatNumber }
        }
    )

    // LocalTime을 HH:mm 형식으로 변환하기 위한 포매터 우리 눈에는 HH:MM으로 보임
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val formattedSchedules = schedules.map { schedule ->
        mapOf(
            "id" to schedule.id,
            "date" to schedule.date,
            "start" to schedule.start.format(timeFormatter),
            "end" to schedule.end.format(timeFormatter),
            "movie" to schedule.movie,
            "theater" to schedule.theater
        )
    }
    println(occupiedSeatsMap)
    model.addAttribute("occupiedSeatsMap", occupiedSeatsMap) // 예약된 좌석 정보 추가
    model.addAttribute("uniqueMovies",uniqueMovies)
    model.addAttribute("schedules", formattedSchedules)  // 모델에 스케줄 리스트 추가
    return "reservation"  // 타임리프를 사용하는 뷰의 이름

}
}