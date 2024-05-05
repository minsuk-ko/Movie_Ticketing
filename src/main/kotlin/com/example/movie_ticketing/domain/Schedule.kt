package com.example.movie_ticketing.domain

import jakarta.persistence.*

@Entity
class Schedule {

    var start : String? = null
    var end : String? = null

    // 영화 상영일
    var date : String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    var movie : Movie? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    var theater : Theater? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    val id : Int = 0

    override fun toString(): String {
        return "Schedule(start=$start, end=$end, date=$date, movie=$movie, theater=$theater, id=$id)"
    }
}