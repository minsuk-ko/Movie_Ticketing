package com.example.movie_ticketing.domain

import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Reservation{

    // 예약 날짜
    lateinit var date : String

    var price : Int = 0

    // 티켓 수량
    var num : Int = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    val id : Int = 0

    constructor() {
    }

    constructor(date: String, price: Int, num: Int, member: Member?) {
        this.date = date
        this.price = price
        this.num = num
        this.member = member
    }
}