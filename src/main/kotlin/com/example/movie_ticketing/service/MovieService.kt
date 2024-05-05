package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.dto.MovieDetails
import com.example.movie_ticketing.repository.MovieRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux


@Service
class MovieService(private val restTemplate: RestTemplate,private val webClient: WebClient,val movieRepository: MovieRepository) {
    @Value("\${tmdb.api.key}")
    private lateinit var apiKey: String


    private val baseUrl = "https://api.themoviedb.org/3/movie"

    fun searchByQuery(query: String): List<Movie> {
        return movieRepository.findByTitleContaining(query)
    }
    fun retrieveMovieDetails(movieId: Int): MovieDetails {
        val url = "$baseUrl/$movieId?api_key=$apiKey"
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
}
