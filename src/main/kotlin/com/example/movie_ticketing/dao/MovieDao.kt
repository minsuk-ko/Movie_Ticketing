package com.example.movie_ticketing.dao

import com.example.movie_ticketing.domain.Movie
import org.springframework.data.domain.Page

interface MovieDao { 
    fun findById(id: Int): Movie?

    fun save(movie: Movie): Movie?

    fun deleteById(id: Int): Int

    fun findByTitleContaining(searchKeyword : String) : Page<Movie>
}