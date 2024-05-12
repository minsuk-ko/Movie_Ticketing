package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.service.GenreService
import com.example.movie_ticketing.service.MovieService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam



@Controller
class MovieController(
    private val movieService: MovieService,
    private val genreService: GenreService
) {

    @GetMapping("/")
    fun showMovies(model: Model): String {
        val movies = movieService.getMoviesFromJson()
        model.addAttribute("movies", movies)
        return "home"
    }

    @GetMapping("/movieInfo/{id}")
    fun showMovieDetails(@PathVariable("id") movieId: Int, model: Model): String {
        val movieDetails = movieService.retrieveMovieDetails(movieId)
        val genreNames = genreService.getGenreNames(movieDetails.genreIds)
        model.addAttribute("movieDetails", movieDetails)
        model.addAttribute("genreNames", genreNames.joinToString(", "))
        return "movieInfo" // Thymeleaf 뷰 파일 이름
    }

    @GetMapping("/search")
    fun searchMovie(@RequestParam("query") query: String, model: Model): String {
        val results = movieService.searchByQuery(query)
        model.addAttribute("movies", results)
        return "movie" // Thymeleaf 뷰 템플릿의 이름
    }
}
