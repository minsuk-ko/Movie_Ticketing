package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.dto.MovieDetails
import com.example.movie_ticketing.service.MovieService
import com.example.movie_ticketing.service.TmdbService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono


@Controller
class MovieController(private val movieService: MovieService) {

    // todo pass(영화목록)
//    @GetMapping("/movie/list")
//    fun movieList(model: Model,
//                  @PageableDefault(page = 0, size = 10, sort = ["id"], direction = Sort.Direction.DESC )
//                  pageable: Pageable, searchKeyword : String? = ""){
//
//        // todo ???
//        var list : Page<Movie?>? = null
//
//        if(searchKeyword == null){
//            // 기존 list
//            list = movieService.movieList(pageable)
//        } else{
//            // 검색 list
//            list = movieService.movieSearchList(searchKeyword, pageable)
//        }
//    }
//    @RestController
//    @RequestMapping("/api/movies")
//    class MovieController(private val tmdbService: TmdbService) {
//
//        @GetMapping("/{movieId}")
//        fun getMovieDetails(@PathVariable movieId: String, model: Model): ResponseEntity<Mono<String>> {
//            val movieDetails = tmdbService.fetchMovieDetails(movieId)
//            model.addAttribute("movieDetails",movieDetails)
//            return ResponseEntity.ok(movieDetails)
//        }
//    }
    @Controller
    class MovieController(private val movieService: MovieService) {

        @GetMapping("/movies/{id}")
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
    }



}