package com.example.movie_ticketing.domain

import jakarta.persistence.*

@Entity
class Seat {

    var row: String? = null
    var col: Int? = null

    var isSelected : Boolean = true

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    var theater: Theater? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    val id: Int = 0

    protected constructor() {
    }

    constructor(row: String?, col: Int?, isSelected: Boolean, theater: Theater?) {
        this.row = row
        this.col = col
        this.isSelected = isSelected
        this.theater = theater
    }
}