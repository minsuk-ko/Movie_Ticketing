package com.example.movie_ticketing.domain

import jakarta.persistence.*
import java.util.Date


@Entity
class Movie {

    val title : String? = null
    val story : String? = null
    val director : String? =null
    val actor : String? = null
    val poster : String? = null
    val openDate : Date? = null
    val runtime : Int? = null

    @Enumerated(EnumType.STRING)
    val rating : Rating? = null // G, PG12, PG15, PG18

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    val id : Long = 0L
}
