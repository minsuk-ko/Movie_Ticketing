package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.repository.MovieRepository
import com.example.movie_ticketing.service.MovieService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping


@Controller
class MovieController(private val movieService: MovieService) {

    @GetMapping("/movie/list")
    fun movieList(model: Model,
                  @PageableDefault(page = 0, size = 10, sort = ["id"], direction = Sort.Direction.DESC )
                  pageable: Pageable, searchKeyword : String? = ""){

        // todo ???
        var list : Page<Movie?>? = null

        if(searchKeyword == null){
            // 기존 list
            list = movieService.movieList(pageable)
        } else{
            // 검색 list
            list = movieService.movieSearchList(searchKeyword, pageable)
        }
    }


//    @PostMapping("/movie/list")


}