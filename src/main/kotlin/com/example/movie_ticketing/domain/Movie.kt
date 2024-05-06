package com.example.movie_ticketing.domain

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "movie")
data class Movie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    val actor: String,
    val director: String,

    val openDate: Date,

    val posterUrl: String,
    val rating: String,
    val runtime: String,
    val state: Boolean,
    val story: String,
    val title: String
)
enum class Rating {
    G,      // 전체관람가
    PG12,   // 12세관람가
    PG15,   // 15세관람가
    PG18;   // 청소년관람불가
}


