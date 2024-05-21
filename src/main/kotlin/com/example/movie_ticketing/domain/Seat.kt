package com.example.movie_ticketing.domain

import jakarta.persistence.*

@Entity
class Seat {

    var seatNumber : Int = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    lateinit var theater: Theater

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0

    override fun toString(): String {
        return "Seat(seatNumber=$seatNumber, theater=$theater, id=$id)"
    }
}

