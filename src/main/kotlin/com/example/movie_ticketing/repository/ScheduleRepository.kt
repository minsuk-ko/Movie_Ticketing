package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.domain.Schedule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalTime

@Repository
interface ScheduleRepository : JpaRepository<Schedule, Int> {
    fun findByMovieIdAndDateAndStartAndTheaterId(movieId: Int, date: LocalDate, start: LocalTime,theaterId:Int): Schedule
    fun save(schedule: Schedule)
    fun findByDateBetween(startDate:LocalDate,endDate: LocalDate):List<Schedule>
    @Query("SELECT s FROM Schedule s WHERE s.theater.id = :theaterId")
    fun findByTheaterId(@Param("theaterId") theaterId: Int): List<Schedule>

    fun getSchedulesByTheaterId(theaterId: Int): List<Schedule>
}