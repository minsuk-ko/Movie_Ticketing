package com.example.movie_ticketing.domain

import jakarta.persistence.*


@Entity
class Seat {

    //좌석 선택
    @Enumerated(EnumType.STRING)
    val SelectStatus : String? = null // POSSIBLE, IMPOSSIBLE

    val row : String? = null
    val column : Int? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    val theater : Theater? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    val id : Long? = null
}
