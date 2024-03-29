package com.example.movie_ticketing.domain

import org.springframework.data.annotation.Id

data class Movie(
    @Id var id : Int = 0,
    var title : String = "",
    var story : String = "",
    var director : String = "",
    var actor : String = "",
    var posterURL : String = "",
    var openDate : String="",
    var runtime : Int =0,
    var rating : Rating,
    var state : Boolean){
}

enum class Rating {
    G,      // 전체관람가
    PG12,   // 12세관람가
    PG15,   // 15세관람가
    PG18;   // 청소년관람불가
}


