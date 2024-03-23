package com.example.movie_ticketing.domain

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime


@Entity
class Schedule {

    val start : String? = null
    val end : String? = null
    val date : LocalDate? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    var movie : Movie? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    val theater : Theater? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    val id : Long = 0L
}
