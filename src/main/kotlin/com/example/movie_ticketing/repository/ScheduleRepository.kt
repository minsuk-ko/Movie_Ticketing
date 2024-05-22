package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.domain.Schedule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ScheduleRepository : JpaRepository<Schedule, Int> {
    fun findByMovieIdAndDateAndStartAndTheaterId(movieId: Int, date: String, start: String,theaterId:Int): Schedule
    fun save(schedule: Schedule)
    fun findByDateBetween(startDate:String,endDate: String):List<Schedule>



}