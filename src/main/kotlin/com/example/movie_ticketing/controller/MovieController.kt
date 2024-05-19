package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.Member
import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.dto.MovieDetails
import com.example.movie_ticketing.service.MovieService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Mono


@Controller
class MovieController(private val movieService: MovieService) {

    @GetMapping("/")
    fun showMovies(model: Model): String {
        val movies = movieService.getMoviesFromJson()
        model.addAttribute("movies", movies)
        return "home"
    }

    @GetMapping("/movieInfo/{id}")
    fun showMovieDetails(@PathVariable("id") movieId: Int, model: Model): String {
        try {
            val movieDetails = movieService.retrieveMovieDetails(movieId)
            val actors = movieService.getTopTwoActorsForMovie(movieId)
            model.addAttribute("movieDetails", movieDetails)
            model.addAttribute("actors", actors)
            return "movieInfo" // Thymeleaf 뷰 파일 이름
        } catch (e: Exception) {
            model.addAttribute("error", "Movie not found")
            return "errorView" // 에러 시 보여줄 뷰
        }
    }

    /**
     * BoxOffice() 에서 가져온 movies 를 List 형식으로 movie.html 에 넘긴다
     */
    @GetMapping("/movie")
    fun movieOffice(@RequestParam(value = "page", defaultValue = "1") page: Int, model: Model): String {
        val movies = movieService.getBoxOffice(page)
        model.addAttribute("movies", movies.movies)
        model.addAttribute("currentPage", page)
        model.addAttribute("totalPages", movies.total_pages)
        return "movie"
    }



    /**
     * 영화 검색
     * 검색 결과 MovieSearchResult 의 movies 를 view 로 보냄 (List 형식)
     */
    @GetMapping("/search")
    fun searchMovie(@RequestParam("query") query: String, @RequestParam(value = "page", defaultValue = "1") page: Int, model: Model): String {
        val searchResult = movieService.searchMovies(query, page)
        model.addAttribute("movies", searchResult.movies)
        model.addAttribute("currentPage", page)
        model.addAttribute("totalPages", searchResult.total_pages)
        return "searchResult"
    }

}
