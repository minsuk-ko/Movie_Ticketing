package com.example.movie_ticketing.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp


@Entity
class Reservation{

    // 예약 날짜
    @CreationTimestamp   //  이렇게 하면 생성한 날짜 바로 뜸
    // 즉, 데이터베이스에 엔티티가 저장될 때 자동으로 시간을 설정
    lateinit var date : Timestamp
    //lateinit var date : String

  //스키마에 주석처리되어있어서 뺌 var price : Int = 0

    // 티켓 수량
  //  @Column(name = )
 //   var num : Int = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    lateinit var member: Member

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  //  @Column(name = "reservation_id")
    val id : Int = 0
    override fun toString(): String {
        return "Reservation(date='$date', member=$member, id=$id)"
    }// price=$price, num=$num
}