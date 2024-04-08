package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Movie
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface MovieRepository : JpaRepository<Movie?, Int?> {

    fun findByTitleContaining(searchKeyword: String?, pageable: Pageable?): Page<Movie?>?
}