package com.example.movie_ticketing.domain

import com.example.movie_ticketing.dto.MovieSearchResult
import jakarta.persistence.*
import java.time.LocalDate
import java.util.Date

@Entity
class Movie {
//나머지값들은 api에서 가져오자!

    @Lob
    lateinit var role:String
    @Lob
    lateinit var cast: String
    lateinit var title: String

    //인기순
    var popularity:Double=0.0
    //예매가능
    var state: Boolean = false

    lateinit var openDate:LocalDate
    @Id
    var id: Int = 0
}
