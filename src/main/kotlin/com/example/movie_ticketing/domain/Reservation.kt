package com.example.movie_ticketing.domain

import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Reservation{

    // 예약 날짜
    lateinit var date : LocalDate

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    lateinit var member: Member

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    val id : Int = 0

    override fun toString(): String {
        return "Reservation(date='$date', member=$member, id=$id)"
    }
}