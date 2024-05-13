package com.example.movie_ticketing.domain

import jakarta.persistence.*
import java.util.Date

@Entity
class Movie {

    //lateinit var actor: String
    //lateinit var director: String
    lateinit var openDate: Date
    lateinit var posterPath: String

    var rating: Double = 0.0

    lateinit var runtime: String
    // 초기값 true 로 설정
    var state: Boolean = true

    lateinit var overview: String
    lateinit var title: String

    // 초기값 false 로 설정
    var adult : Boolean = false

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0

    //override fun toString(): String {
     //   return "Movie(actor='$actor', director='$director', openDate=$openDate, posterUrl='$posterUrl'," +
     //           "rating=$rating, runtime='$runtime', state=$state, story='$story', title='$title', isAdult=$isAdult, id=$id)"
    //}
}

