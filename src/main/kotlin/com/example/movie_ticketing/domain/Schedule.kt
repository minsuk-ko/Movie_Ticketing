package com.example.movie_ticketing.domain

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
class Schedule {

    lateinit var start : LocalTime
    lateinit var end : LocalTime

    // 영화 상영일
    lateinit var date: LocalDate

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    lateinit var movie : Movie

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    lateinit var theater: Theater

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Int =0

    override fun toString(): String {
        return "Schedule(start=$start, end=$end, date=$date, movie=$movie, theater=$theater, id=$id)"
    }
}