package com.example.movie_ticketing.controller

import com.example.movie_ticketing.dto.MovieDetails
import com.example.movie_ticketing.service.MovieService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.core.io.ClassPathResource
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import logger
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class HomeController {

    val log = logger()

    @RequestMapping("/")
    fun home(): String {
        log.info("home controller")
        return "home"
    }
    @Controller
    class MovieController(private val movieService: MovieService) {

        @GetMapping("/")
        fun showMovies(model: Model): String {
            val movies: List<MovieDetails> = movieService.getMoviesFromJson()
            model.addAttribute("movies", movies)
            return "home"
        }
    }

}