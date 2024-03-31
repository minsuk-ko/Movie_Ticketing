package com.example.movie_ticketing.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Movie{
    var title : String = ""
    var story : String = ""
    var director : String = ""
    var actor : String = ""
    var posterURL : String = ""
    var openDate : String=""
    var runtime : Int =0
    var rating : Rating = Rating.G
    var state : Boolean = true


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Int = 0
}

enum class Rating {
    G,      // 전체관람가
    PG12,   // 12세관람가
    PG15,   // 15세관람가
    PG18;   // 청소년관람불가
}


