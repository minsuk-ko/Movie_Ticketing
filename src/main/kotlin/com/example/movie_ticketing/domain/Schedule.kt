package com.example.movie_ticketing.domain

import jakarta.persistence.*

@Entity
class Schedule {

    lateinit var start : String
    lateinit var end : String

    // 영화 상영일
    lateinit var date : String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    lateinit var movie : Movie

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    lateinit var theater : Theater

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Int = 0

    override fun toString(): String {
        return "Schedule(start=$start, end=$end, date=$date, movie=$movie, theater=$theater, id=$id)"
    }
}