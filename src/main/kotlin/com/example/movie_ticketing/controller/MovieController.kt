package com.example.movie_ticketing.controller

import com.example.movie_ticketing.repository.MovieRepository
import com.example.movie_ticketing.service.MovieService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate
import java.time.LocalDateTime


@Controller
class MovieController(private val movieService: MovieService,
                        private val movieRepository: MovieRepository) {

    @GetMapping("/")
    fun showMovies(model: Model): String {
        val movies = movieService.getMoviesFromJson()
        model.addAttribute("movies", movies)
        return "home"
    }

    @GetMapping("/movieInfo/{id}")
    fun showMovieDetails(@PathVariable("id") movieId: Int, model: Model): String {
        try {val movie = movieRepository.findById(movieId)
            var state = false
            val movieDetails = movieService.retrieveMovieDetails(movieId)
            val actors = movieService.getCast(movieId)
            if(movie.isPresent) //Optional movie가 존재한다면!
            {state =movie.get().state}
            model.addAttribute("state",state)
            model.addAttribute("movieDetails", movieDetails)
            model.addAttribute("actors", actors)
            return "movieInfo" // Thymeleaf 뷰 파일 이름
        } catch (e: Exception) {
            model.addAttribute("error", "Movie not found")
            return "error" // 에러 시 보여줄 뷰
        }
    }


    /**
     * BoxOffice() 에서 가져온 movies 를 List 형식으로 movie.html 에 넘긴다
     */
    @GetMapping("/movie")
    fun movieOffice(@RequestParam(value = "page", defaultValue = "1") page: Int, model: Model): String {

        val currentDate = LocalDate.now()
        val movies = movieService.getBoxOffice(currentDate,page)
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
