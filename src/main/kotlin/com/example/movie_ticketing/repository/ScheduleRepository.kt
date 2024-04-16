package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Schedule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ScheduleRepository :JpaRepository<Schedule, Int >{
    fun findByDate(date: LocalDateTime): List<Schedule>
}