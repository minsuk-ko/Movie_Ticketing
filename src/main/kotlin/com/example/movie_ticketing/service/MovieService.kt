package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.dto.MovieDetails
import com.example.movie_ticketing.dto.MovieResponse
import com.example.movie_ticketing.repository.MovieRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.io.InputStream


@Service
class MovieService(private val restTemplate: RestTemplate,private val webClient: WebClient,val movieRepository: MovieRepository,private val objectMapper: ObjectMapper) {
    @Value("\${tmdb.api.key}")
    private lateinit var apiKey: String


    private val baseUrl = "https://api.themoviedb.org/3/movie/"

    fun searchByQuery(query: String): List<Movie> {
        return movieRepository.findByTitleContaining(query)
    }
    fun retrieveMovieDetails(movieId: Int): MovieDetails {
        // URL 뒤에 language=ko-KR 을 통해 한글로 가져온다.
        val url = "$baseUrl/$movieId?api_key=$apiKey&language=ko-KR"
        return restTemplate.getForObject(url, MovieDetails::class.java) ?: throw Exception("Movie not found")
    }
    fun searchMovies(query: String): Flux<MovieDetails> {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/search/movie")
                    .queryParam("api_key", apiKey)
                    .queryParam("query", query)
                    .build()
            }
            .retrieve()  // API 호출을 수행하고 응답을 가져옴
            .bodyToFlux(MovieDetails::class.java)  // 응답 본문을 Movie 클래스의 Flux로 변환
    }
    fun getMoviesFromJson(): List<MovieDetails> {
        val jsonInputStream: InputStream = ClassPathResource("movies.json").inputStream
        val movieResponse: MovieResponse = objectMapper.readValue(jsonInputStream)
        return movieResponse.results
    }
}
