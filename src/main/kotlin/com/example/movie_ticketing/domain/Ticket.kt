package com.example.movie_ticketing.domain

import jakarta.persistence.*

@Entity
class Ticket {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    lateinit var schedule: Schedule

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    lateinit var seat: Seat

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    lateinit var theater: Theater

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    lateinit var reservation: Reservation

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Int = 0

    // ReservationController 의 오류 해결을 위해 constructor 가 아닌 toString 으로 바꾸었음
    override fun toString(): String {
        return "Ticket(schedule=$schedule, seat=$seat, reservation=$reservation, id=$id)"
    }
}