package com.example.movie_ticketing.domain

import jakarta.persistence.*
import java.util.Date

@Entity
class Movie {

    lateinit var actor: String
    lateinit var director: String
    lateinit var openDate: Date
    lateinit var posterUrl: String

    var rating: Double = 0.0

    lateinit var runtime: String
    // 초기값 true 로 설정
    var state: Boolean = true

    lateinit var story: String
    lateinit var title: String

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0

    override fun toString(): String {
        return "Movie(actor='$actor', director='$director', openDate=$openDate, posterUrl='$posterUrl', rating=$rating, runtime='$runtime', state=$state, story='$story', title='$title', id=$id)"
    }

}

enum class Rating {
    G,      // 전체관람가
    PG12,   // 12세관람가
    PG15,   // 15세관람가
    PG18;   // 청소년관람불가
}


