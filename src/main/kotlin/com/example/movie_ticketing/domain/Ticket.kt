package com.example.movie_ticketing.domain

import jakarta.persistence.*

@Entity
class Ticket {

    var price : Int = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    var schedule: Schedule? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    var seat: Seat? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    var reservation: Reservation? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Int = 0

    protected constructor() {
    }

    constructor(price: Int, schedule: Schedule?, seat: Seat?, reservation: Reservation?) {
        this.price = price
        this.schedule = schedule
        this.seat = seat
        this.reservation = reservation
    }
}