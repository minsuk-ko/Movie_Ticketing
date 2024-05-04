package com.example.movie_ticketing.domain

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "movie")
data class Movie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val actor: String,
    val director: String,

    @Column(name = "open_date")
    val openDate: Date,

    val posterUrl: String,
    val rating: Double,
    val runtime: Int,
    val state: String,
    val story: String,
    val title: String
)

enum class Rating {
    G,      // 전체관람가
    PG12,   // 12세관람가
    PG15,   // 15세관람가
    PG18;   // 청소년관람불가
}


