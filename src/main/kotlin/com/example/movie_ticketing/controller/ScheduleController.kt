package com.example.movie_ticketing.controller

import com.example.movie_ticketing.repository.MovieRepository
import com.example.movie_ticketing.repository.ScheduleRepository
import com.example.movie_ticketing.repository.TheaterRepository
import com.example.movie_ticketing.service.MovieService
import com.example.movie_ticketing.service.ScheduleService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.time.LocalDate


@Controller
class ScheduleController( private val scheduleService: ScheduleService,
                         private val scheduleRepository: ScheduleRepository,
                        private val theaterRepository: TheaterRepository,
                        private val movieRepository: MovieRepository) {

@GetMapping("/reservation")  //영화 db가 있어야해서 일단 영화부터 받음
fun viewReservation(model: Model):String{
    if(theaterRepository.findAll().isEmpty()) { //상영관 없으면 바로 생성
    scheduleService.initializeTheaters()
    }
    if(scheduleRepository.findAll().isEmpty()){

       scheduleService.createSchedule() //스케줄 30일치 생성

    }
    val currentDate = LocalDate.now()
    val endDate = currentDate.plusWeeks(2)  // 현재로부터 2주 후의 날짜
    val schedules =  scheduleRepository.findByDateBetween(currentDate.toString(),endDate.toString())// 리포지토리에서 2주치 꺼내오기
    // 영화별로 중복을 제거한 리스트 생성
    val uniqueMovies = schedules.map { it.movie }.distinctBy { it.id } //이래야지 리스트에는 상영하는 영화만 보임
    model.addAttribute("uniqueMovies",uniqueMovies)
    model.addAttribute("schedules", schedules)  // 모델에 스케줄 리스트 추가
    return "reservation"  // 타임리프를 사용하는 뷰의 이름

}
}