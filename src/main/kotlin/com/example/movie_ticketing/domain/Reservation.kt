package com.example.movie_ticketing.domain

import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Reservation{

    // 예약 날짜
    lateinit var date : String

  //스키마에 주석처리되어있어서 뺌 var price : Int = 0

    // 티켓 수량
  //  @Column(name = )
    var num : Int = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    lateinit var member: Member

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  //  @Column(name = "reservation_id")
    val id : Int = 0
    override fun toString(): String {
        return "Reservation(date='$date',, num=$num, member=$member, id=$id)"
    }// price=$price
}