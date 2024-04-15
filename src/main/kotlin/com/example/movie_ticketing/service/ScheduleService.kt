package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Schedule
import com.example.movie_ticketing.repository.MovieRepository
import com.example.movie_ticketing.repository.ScheduleRepository
import com.example.movie_ticketing.repository.TheaterRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ScheduleService (
    private val scheduleRepository: ScheduleRepository,
    private val movieRepository: MovieRepository,
    private val theaterRepository: TheaterRepository
){
    @Transactional
    fun createSchedule(movieId: Int, theaterId: Int, start: LocalDateTime, end: LocalDateTime, date: LocalDateTime): Schedule {
        val movie = movieRepository.findById(movieId).orElseThrow {
            IllegalArgumentException("영화가 없습니다")
        }
        val theater = theaterRepository.findById(theaterId).orElseThrow {
            IllegalArgumentException("상영관이 없습니다")
        }

        // Check for schedule conflicts
        val existingSchedules = scheduleRepository.findByDate(date)
        for (schedule in existingSchedules) {
            // 좌석 확인하는 거 확인하는건 컨트롤러에서
        }

        val newSchedule = Schedule(start, end, date, movie, theater)
        return scheduleRepository.save(newSchedule)
    }

    fun findSchedulesByDate(date: LocalDateTime): List<Schedule> {
        return scheduleRepository.findByDate(date)
    }
}