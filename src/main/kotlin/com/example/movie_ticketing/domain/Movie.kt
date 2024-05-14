package com.example.movie_ticketing.domain

import com.example.movie_ticketing.dto.MovieSearchResult
import jakarta.persistence.*
import java.time.LocalDate
import java.util.Date

@Entity
class Movie {
//나머지값들은 api에서 가져오자!
    lateinit var role:String
    lateinit var cast: String
    lateinit var title: String

    //인기순
    var popularity:Double=0.0
    //예매가능
    var state: Boolean = false

    lateinit var openDate:LocalDate
    var tmdbid:Int = 0
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0

    /*
    override fun toString(): String {
        return "Movie(actor='$actor', director='$director', openDate=$openDate, posterUrl='$posterUrl'," +
                " runtime='$runtime', state=$state, story='$story', title='$title', isAdult=$isAdult, id=$id)"
    }

     */
    //rating=$rating,
}
