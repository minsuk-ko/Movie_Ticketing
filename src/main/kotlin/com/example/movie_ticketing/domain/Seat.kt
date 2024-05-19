package com.example.movie_ticketing.domain

import jakarta.persistence.*

@Entity
class Seat {

    val seatNumber : Int = 0

    // 처음에는 예약된 좌석이 없으므로 false
    var isSelected : Boolean = false

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    lateinit var theater: Theater

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    val id: Int = 0

    override fun toString(): String {
        return "Seat(seatNumber=$seatNumber, isSelected=$isSelected, theater=$theater, id=$id)"
    }
}
