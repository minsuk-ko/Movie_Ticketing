package com.example.movie_ticketing.domain

import jakarta.persistence.*
import java.util.Date


@Entity
class Movie {

    var title : String? = null
    var story : String? = null
    var director : String? =null
    var actor : String? = null
    var poster : String? = null
    var openDate : Date? = null
    var runtime : Int? = null

    @Enumerated(EnumType.STRING)
    var rating : Rating? = null // G, PG12, PG15, PG18

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    val id : Long = 0L

    protected constructor() {
    }

    constructor(
        title: String?,
        story: String?,
        director: String?,
        actor: String?,
        poster: String?,
        openDate: Date?,
        runtime: Int?,
        rating: Rating?
    ) {
        this.title = title
        this.story = story
        this.director = director
        this.actor = actor
        this.poster = poster
        this.openDate = openDate
        this.runtime = runtime
        this.rating = rating
    }
}
