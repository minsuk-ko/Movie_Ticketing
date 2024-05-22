package com.example.movie_ticketing.repository


import com.example.movie_ticketing.domain.Seat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


interface SeatRepository  : JpaRepository<Seat, Long> {

}