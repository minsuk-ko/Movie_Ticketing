package com.example.movie_ticketing.domain

import jakarta.persistence.*


@Entity
class Seat {

    val row : String? = null
    val column : Int? = null
    //좌석 선택

    @Enumerated(EnumType.STRING)
    val selectStatus : SelectStatus? = null // POSSIBLE, IMPOSSIBLE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    val theater : Theater? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    val id : Long? = null
}
