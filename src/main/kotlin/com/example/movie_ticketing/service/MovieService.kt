package com.example.movie_ticketing.service

import com.example.movie_ticketing.config.ApiConfiguration
import com.example.movie_ticketing.dto.MovieDetails
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class MovieService(private val restTemplate: RestTemplate) {
    @Value("\${tmdb.api.key}")
    private lateinit var apiKey: String

    private val baseUrl = "https://api.themoviedb.org/3/movie"

    fun retrieveMovieDetails(movieId: Int): MovieDetails {
        val url = "$baseUrl/$movieId?api_key=$apiKey"
        return restTemplate.getForObject(url, MovieDetails::class.java) ?: throw Exception("Movie not found")
    }
}
