package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Member
import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.domain.Reservation
import com.example.movie_ticketing.domain.Seat
import com.example.movie_ticketing.repository.MemberRepository
import com.example.movie_ticketing.repository.MovieRepository
import com.example.movie_ticketing.repository.ReservationRepository
import com.example.movie_ticketing.repository.SeatRepository
import org.hibernate.id.enhanced.Optimizer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.Optional

@Service
class ReservationService(
    private val reservationRepository : ReservationRepository,
    private val seatRepository: SeatRepository,
    private val memberRepository: MemberRepository,
    private val movieRepository: MovieRepository
) {

    // 영화 날짜 시간 선택까지만
    @Transactional
    fun selectMovieAndDateAndTime(movie : Movie , reservation: Reservation) : Reservation {
        val movieTitle = movieRepository.findById(movie.id)
        val reservationMovie = reservationRepository.findById(reservation.id)

        movieRepository.save(movieTitle)
        reservationRepository.save(reservationMovie)

        return reservation
    }
}