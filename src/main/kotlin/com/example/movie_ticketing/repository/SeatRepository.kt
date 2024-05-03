package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Seat
import com.example.movie_ticketing.domain.Theater
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SeatRepository : JpaRepository<Seat, Int> {

    fun findByTheater(theater : Theater): List<Seat>
    fun save(seatNum: Optional<Seat>)

    // 만약 [1, 2, 3] 이라는 리스트를 매개변수로 전달하면
    // SELECT * FROM table WHERE id IN (1, 2, 3); 과 같은 말
    fun findByIdIn(seatIds: List<Int>): List<Seat>
}