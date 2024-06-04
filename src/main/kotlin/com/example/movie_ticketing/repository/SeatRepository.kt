package com.example.movie_ticketing.repository


import com.example.movie_ticketing.domain.Schedule
import com.example.movie_ticketing.domain.Seat
import com.example.movie_ticketing.domain.Theater
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SeatRepository  : JpaRepository<Seat, Int> {
    fun findByTheaterAndSeatNumber(theater:Theater,seatNumber: Int):Seat
    @Query("SELECT s FROM Schedule s WHERE s.theater.id = :theaterId")
    fun findByTheaterId(@Param("theaterId") theaterId: Int): List<Schedule>
}