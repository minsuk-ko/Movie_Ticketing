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
    val member: Member? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    val schedule : Schedule? = null


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    val seat : Seat? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_id")
    val id : Long = 0L
}