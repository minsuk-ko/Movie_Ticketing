package com.example.movie_ticketing.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDate
import java.time.LocalTime


@Entity
class Schedule {

    val start : LocalTime? = null
    val end : LocalTime? = null
    val date : LocalDate? = null

    @ManyToOne
    @JoinColumn(name = "movie_id")
    var movie : Movie? = null

    @ManyToOne
    @JoinColumn(name = "theater_id")
    val theater : Theater? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    val id : Long = 0L
}
