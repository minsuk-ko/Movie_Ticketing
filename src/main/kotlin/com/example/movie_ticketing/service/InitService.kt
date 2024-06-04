package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Seat
import com.example.movie_ticketing.repository.ScheduleRepository
import com.example.movie_ticketing.repository.SeatRepository
import com.example.movie_ticketing.repository.TheaterRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class InitService(private val movieService: MovieService,
                   private  val theaterRepository: TheaterRepository,
                    private val scheduleService: ScheduleService,
                    private val seatRepository: SeatRepository,
                    private val scheduleRepository: ScheduleRepository)  {

    @PostConstruct
    fun init(){  //reservation 먼저 생성하기 위해서 사용

        movieService.getBoxOffice(LocalDate.now(),1)
        if(theaterRepository.findAll().isEmpty()) { //상영관 없으면 바로 생성
            scheduleService.initializeTheaters()
        }
        if(seatRepository.findAll().isEmpty()){
            val theaters =theaterRepository.findAll()     //좌석 없으면 바로 생성
            for(theater in theaters)
                for(i:Int in 1..49) //1에서 49까지
                { val seat= Seat()
                    seat.theater= theater
                    seat.seatNumber = i
                    seatRepository.save(seat)

                }
        }
        if(scheduleRepository.findAll().isEmpty()){

            scheduleService.createSchedule() //스케줄 30일치 생성

        }
    }
}