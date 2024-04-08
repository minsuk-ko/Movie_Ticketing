package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.repository.MovieRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class MovieService(var movieRepository: MovieRepository) {

    /**
     * 검색
     */
    fun movieList(pageable: Pageable): Page<Movie?> {
        return movieRepository.findAll(pageable)
    }

    fun movieSearchList(searchKeyword : String, pageable: Pageable) : Page<Movie?>? {
        return movieRepository.findByTitleContaining(searchKeyword,pageable)
    }

    /**
     *  list
     */



}