package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.dto.MovieDetails
import com.example.movie_ticketing.dto.MovieResponse
import com.example.movie_ticketing.dto.MovieSearchResult
import com.example.movie_ticketing.repository.MovieRepository
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue


import java.io.InputStream


@Service
class MovieService(private val restTemplate: RestTemplate,private val webClient: WebClient,val movieRepository: MovieRepository) {
    @Value("\${tmdb.api.key}")
    private lateinit var apiKey: String

    @Value("\${tmdb.api.url}")
    private val baseUrl = "https://api.themoviedb.org/3/movie/"
    private val objectMapper: ObjectMapper = jacksonObjectMapper().apply { // 오브젝트매퍼는 잭슨 내부의 라이브러리로 json을 클래스처럼 만들어주는 메서드
        findAndRegisterModules() // 모듈화
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // 당장 안쓰는값 무시하는 함수
    }

    fun searchByQuery(query: String): List<Movie> {
        return movieRepository.findByTitleContaining(query) // 쿼리로 검색하는 함수 title을 매개로 받도록 리포지토리에서 선언
    }
    fun retrieveMovieDetails(movieId: Int): MovieDetails { // 동적으로 값을 가져오는 함수
        // URL 뒤에 language=ko-KR 을 통해 한글로 가져온다.
        val url = "$baseUrl/$movieId?api_key=$apiKey&language=ko-KR" // 가져오는 위치
        return restTemplate.getForObject(url, MovieDetails::class.java) ?: throw Exception("Movie not found")
    }
//    fun searchMovies(query: String): Mono<String> {
//        return webClient.get()
//            .uri { uriBuilder ->
//                uriBuilder.path("/search/movie")
//                    .queryParam("api_key", apiKey)
//                    .queryParam("query", query)
//                    .build()
//            }
//            .retrieve()  // API 호출을 수행하고 응답을 가져옴
//            .bodyToMono(String::class.java)  // 응답 본문을 Movie 클래스의 Flux로 변환
//    }

    fun searchMovies(query: String): MovieSearchResult {
        val uri = "https://api.themoviedb.org/3/search/movie?api_key=$apiKey&query=$query&language=ko-KR" // 쿼리로 영화를 검색하는 uri의 위치
        return restTemplate.getForObject(uri, MovieSearchResult::class.java) ?: throw Exception("Movie not found")
    }
    fun getMoviesFromJson(): List<MovieDetails> { // 비동기적으로 영화를 불러오는 방법
        val jsonInputStream: InputStream = ClassPathResource("movies.json").inputStream
        val movieResponse: MovieResponse = objectMapper.readValue(jsonInputStream)
        return movieResponse.results
    }
}
