package com.example.movie_ticketing.domain

import jakarta.persistence.*

@Entity
class Ticket {
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

    // ReservationController 의 오류 해결을 위해 constructor 가 아닌 toString 으로 바꾸었음
    override fun toString(): String {
        return "Ticket(schedule=$schedule, seat=$seat, reservation=$reservation, id=$id)"
    }
}