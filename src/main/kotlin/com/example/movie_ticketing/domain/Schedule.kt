package com.example.movie_ticketing.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "schedule")
class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    val id: Int = 0  // default value to ensure non-null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    var movie: Movie? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    var theater: Theater? = null

    @Column(nullable = false)
    var start: LocalDateTime
    @Column(nullable = false)
    var end: LocalDateTime

    @Column(nullable = false)
    var date: LocalDateTime

    // JPA requires a no-argument constructor for every entity. Make it private to prevent misuse.
    private constructor() : this(LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), null, null)

    constructor(start: LocalDateTime, end: LocalDateTime, date: LocalDateTime, movie: Movie?, theater: Theater?) {
        this.start = start
        this.end = end
        this.date = date
        this.movie = movie
        this.theater = theater
    }
}