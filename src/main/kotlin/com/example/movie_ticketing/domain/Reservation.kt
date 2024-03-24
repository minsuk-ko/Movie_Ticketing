package com.example.movie_ticketing.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDate


@Entity
class Reservation{

    // 예약 날짜
    var res_date : LocalDate? = null

    // 상영 날짜
    var date : LocalDate? = null

    var price : Int? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    var schedule : Schedule? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    var seat : Seat? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_id")
    val id : Long = 0L

    protected constructor() {
    }

    constructor(
        res_date: LocalDate?,
        date: LocalDate?,
        price: Int?,
        member: Member?,
        schedule: Schedule?,
        seat: Seat?
    ) {
        this.res_date = res_date
        this.date = date
        this.price = price
        this.member = member
        this.schedule = schedule
        this.seat = seat
    }
}
