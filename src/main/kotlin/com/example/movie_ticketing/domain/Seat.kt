package com.example.movie_ticketing.domain

import jakarta.persistence.*

@Entity
class Seat {

    val seatNumber : Int = 0

    var isSelected : Boolean = true

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    var theater: Theater? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    val id: Int = 0

    override fun toString(): String {
        return "Seat(seatNumber=$seatNumber, isSelected=$isSelected, theater=$theater, id=$id)"
    }
}