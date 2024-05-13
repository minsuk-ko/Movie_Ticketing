package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.dto.MovieDetails
import com.example.movie_ticketing.service.MovieService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Mono


@Controller
class MovieController(private val movieService: MovieService) {

    @GetMapping("/movieInfo/{id}")
    fun showMovieDetails(@PathVariable("id") movieId: Int, model: Model): String {
        try {
            val movieDetails = movieService.retrieveMovieDetails(movieId)
            model.addAttribute("movieDetails", movieDetails)
            return "movieInfo" // Thymeleaf 뷰 파일 이름
        } catch (e: Exception) {
            model.addAttribute("error", "Movie not found")
            return "errorView" // 에러 시 보여줄 뷰
        }
    }
    @GetMapping("/movie")
    fun search(model: Model): String {
        val movies = movieService.getBoxOffice()
        println(movies)
        model.addAttribute("movieList", movies)
        return "movie"
    }

    /**
     * 영화 검색
     */
    @GetMapping("/search")
    fun searchMovie(@RequestParam("query") query: String, model: Model): String  {
        val searchResult = movieService.searchMovies(query)
        model.addAttribute("movies", searchResult.movies)
        return "searchResult"
    }
}
