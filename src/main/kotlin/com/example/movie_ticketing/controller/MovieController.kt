package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.Movie
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
    fun search(): String {
        return "movie"
    }

    @GetMapping("/search")
    fun searchMovie(@RequestParam("query") query: String): String {
//        val results = movieService.searchMovies(query)
//        model.addAttribute("movies", results)
//        return "movie" // Thymeleaf 뷰 템플릿의 이름
        return movieService.searchMovies(query)
    }
}
