package com.example.movie_ticketing.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne


@Entity
class Seat {

//    초기설정값 false
    val isSelected : Boolean = false

    val row : String? = null
    val column : Int? = null

    @ManyToOne
    @JoinColumn(name = "theater_id")
    val theater : Theater? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    val id : Long? = null
}
