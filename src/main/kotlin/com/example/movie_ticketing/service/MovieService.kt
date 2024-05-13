package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.dto.MovieDetails
import com.example.movie_ticketing.dto.MovieSearchResult
import com.example.movie_ticketing.repository.MovieRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient


@Service
class MovieService(private val restTemplate: RestTemplate,private val webClient: WebClient,val movieRepository: MovieRepository) {
    @Value("\${tmdb.api.key}")
    private lateinit var apiKey: String

    fun retrieveMovieDetails(movieId: Int): MovieDetails {
        // URL 뒤에 language=ko-KR 을 통해 한글로 가져온다.
        val url = "https://api.themoviedb.org/3/movie/$movieId?api_key=$apiKey&language=ko-KR"
        println(url)
        return restTemplate.getForObject(url, MovieDetails::class.java) ?: throw Exception("Movie not found")
    }

    fun searchMovies(query: String): MovieSearchResult {
        val url = "https://api.themoviedb.org/3/search/movie?api_key=$apiKey&query=$query&language=ko-KR"
        // restTemplate.getForObject : URL로부터 객체를 가져오는 데 사용
        // MovieSearchResult::class.java : restTemplate.getForObject 메소드가 TMDB API로부터 반환된 JSON 응답을
        // MovieSearchResult 타입의 객체로 변환하도록 함.
        val result = restTemplate.getForObject(url, MovieSearchResult::class.java) ?: throw Exception("Movie not found")
        return sortMoviesByPopularity(result)
    }

    /**
     * 검색 결과를 인기도 순으로 정렬
     */
    fun sortMoviesByPopularity(result: MovieSearchResult): MovieSearchResult {
        // movies 리스트를 인기도순으로 내림차순으로 정렬
        // 인기도가 null 이라면 가장 낮은 값으로 설정
        val sortedMovies = result.movies.sortedByDescending { it.popularity ?: Double.MIN_VALUE }
        return MovieSearchResult(sortedMovies)
    }

    // 개봉일이 2024-05-01 일 이후이고 지역이 한국인 영화를 찾아옴.
    // MovieSearchResult 의 반환값이 List<MovieDetails> 이기 때문에 thymeleaf 문법으로 movie.posterPath 하면 되지 않나?
    // 왜 안되는거지? (movie.html)
    fun getBoxOffice() : MovieSearchResult {
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=ko-KR&region=KR&release_date.gte=2024-05-01"
        val result = restTemplate.getForObject(url, MovieSearchResult::class.java) ?: throw Exception("API 영화 호출 실패")
        return result
    }
}
