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
    var reservationDate : LocalDate? = null

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
    @Column(name = "reservation_id")
    val id : Long = 0L

    protected constructor() {
    }

    constructor(
        reservationDate: LocalDate?,
        date: LocalDate?,
        price: Int?,
        member: Member?,
        schedule: Schedule?,
        seat: Seat?
    ) {
        this.reservationDate = reservationDate
        this.date = date
        this.price = price
        this.member = member
        this.schedule = schedule
        this.seat = seat
    }

//    //== 생성 메서드 ==// 폐기
//    fun createReservation(member: Member, schedule: Schedule, seat: Seat): Reservation {
//        val reservation = Reservation()
//        reservation.member = member
//        reservation.schedule = schedule
//        reservation.seat = seat
//        reservation.reservationDate = LocalDate.now()
//        // 영화 상영 날짜와 예매 가격을 어떻게 써야되지
//        reservation.date = Movie.
//        reservation.price =
//        return Reservation()
//    }
}
