package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.Seat
import com.example.movie_ticketing.domain.Ticket
import com.example.movie_ticketing.repository.*
import com.example.movie_ticketing.service.MovieService
import com.example.movie_ticketing.service.ScheduleService
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Controller
class ScheduleController( private val scheduleService: ScheduleService,
                         private val scheduleRepository: ScheduleRepository,
                        private val theaterRepository: TheaterRepository,
                       private val seatRepository: SeatRepository,
                        private val ticketRepository: TicketRepository) {

@GetMapping("/reservation")  //영화 db가 있어야해서 일단 영화부터 받음
fun viewReservation(model: Model):String{

    if(theaterRepository.findAll().isEmpty()) { //상영관 없으면 바로 생성
    scheduleService.initializeTheaters()
    }
    if(seatRepository.findAll().isEmpty()){
           val theaters =theaterRepository.findAll()     //좌석 없으면 바로 생성
        for(theater in theaters)
            for(i:Int in 1..49) //1에서 49까지
            { val seat=Seat()
                seat.theater= theater
                seat.seatNumber = i
                seatRepository.save(seat)

            }
    }
    if(scheduleRepository.findAll().isEmpty()){

       scheduleService.createSchedule() //스케줄 30일치 생성

    }

    val currentDate = LocalDate.now()
    val endDate = currentDate.plusWeeks(2)  // 현재로부터 2주 후의 날짜
    val schedules =  scheduleRepository.findByDateBetween(currentDate,endDate)// 리포지토리에서 2주치 꺼내오기
    // 영화별로 중복을 제거한 리스트 생성
    val uniqueMovies = schedules.map { it.movie }.distinctBy { it.id } //이래야지 리스트에는 상영하는 영화만 보임

    // 각 스케줄에 대한 예약된 좌석 정보를 맵으로 저장
    val occupiedSeatsMap = schedules.associateBy(
        { it.id },
        { schedule ->
            ticketRepository.findBySchedule(schedule).map { it.seat.seatNumber }
        }
    )

    // LocalTime을 HH:mm 형식으로 변환하기 위한 포매터
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