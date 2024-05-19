package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Movie
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*


@Repository
interface MovieRepository : JpaRepository<Movie?, Int?> {

    fun save(movieTitle: Optional<Movie?>)
    fun findTop10ByStateTrueAndOpenDateBeforeOrderByPopularityDesc(localDate: LocalDate):List<Movie>
}