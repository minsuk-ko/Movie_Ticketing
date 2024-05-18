package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.dto.MovieDetails
import com.example.movie_ticketing.dto.MovieResponse
import com.example.movie_ticketing.dto.MovieSearchResult
import com.example.movie_ticketing.repository.MovieRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.core.io.ClassPathResource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import java.io.InputStream


@Service
class MovieService(private val restTemplate: RestTemplate,
                   private val webClient: WebClient,
                   val movieRepository: MovieRepository,
                   private val objectMapper: ObjectMapper
) {
    @Value("\${tmdb.api.key}")
    private lateinit var apiKey: String

    fun retrieveMovieDetails(movieId: Int): MovieDetails {
        // URL 뒤에 language=ko-KR 을 통해 한글로 가져온다.
        val url = "https://api.themoviedb.org/3/movie/$movieId?api_key=$apiKey&language=ko-KR"
        return restTemplate.getForObject(url, MovieDetails::class.java) ?: throw Exception("Movie not found")
    }

    /**
     * restTemplate.getForObject : URL로부터 객체를 가져오는 데 사용
     * MovieSearchResult::class.java : restTemplate.getForObject 메소드가 TMDB API로부터 반환된 JSON 응답을
     * MovieSearchResult 타입의 객체로 변환하도록 함.
     */
    fun searchMovies(query: String, page: Int): MovieSearchResult {
        val url = "https://api.themoviedb.org/3/search/movie?api_key=$apiKey&query=$query&language=ko-KR&region=KR&release_date.gte=2024-05-01&release_date.lte=2024-06-01&page=$page"
        val result = restTemplate.getForObject(url, MovieSearchResult::class.java) ?: throw Exception("Movie not found")
        return sortMoviesByPopularity(result)
    }


    /**
     * 검색 결과를 인기도 순으로 정렬
     */
    private fun sortMoviesByPopularity(result: MovieSearchResult): MovieSearchResult {
        val sortedMovies = result.movies.sortedByDescending { it.popularity }
        return MovieSearchResult(
            page = result.page,
            movies= sortedMovies,
            total_pages = result.total_pages,
            total_results = result.total_results
        )
    }

    // 개봉일이 2024-05-01 ~ 2024-06-01일 사이이면서 지역이 한국인 영화를 찾아옴.
    // MovieSearchResult 의 반환값이 List<MovieDetails> 이기 때문에 thymeleaf 문법으로 ${movie.posterPath} 할 수 있음
    fun getBoxOffice(page: Int): MovieSearchResult {
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=ko-KR&region=KR" +
                "&release_date.gte=2024-05-01&release_date.lte=2024-06-01&page=$page&include_adult=false&vote_average.gte=1"

        val result = restTemplate.getForObject(url, MovieSearchResult::class.java) ?: throw Exception("API 영화 호출 실패")
        return sortMoviesByPopularity(result)
    }

    fun getMoviesFromJson(): List<MovieDetails> {
        val jsonInputStream: InputStream = ClassPathResource("movies.json").inputStream
        val movieResponse: MovieResponse = objectMapper.readValue(jsonInputStream)
        return movieResponse.results
    }

    fun getTopTwoActorsForMovie(movieId: Int): List<String> {
        val url = "https://api.themoviedb.org/3/movie/$movieId/credits?api_key=$apiKey&language=ko-KR"
        val response = restTemplate.getForObject(url, String::class.java) ?: throw Exception("Actors not found")
        val jsonResponse = JSONObject(response)
        val castArray = jsonResponse.getJSONArray("cast")

        val actorsList = mutableListOf<String>()
        for (i in 0 until castArray.length()) {
            val actor = castArray.getJSONObject(i)
            actorsList.add(actor.getString("name"))
        }
 
        return actorsList.take(5)  // 이제 정상적으로 take 사용 가능
    }

 
}
