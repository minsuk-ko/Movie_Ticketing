package com.example.movie_ticketing.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.Fetch
import java.time.LocalDate


@Entity
class Reservation{

    val date : LocalDate? = null
    val price : Int? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    var seat : Seat? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    var schedule : Schedule? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_id")
    val id : Long = 0L
}