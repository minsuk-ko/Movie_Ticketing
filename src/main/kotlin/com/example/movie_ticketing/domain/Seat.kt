package com.example.movie_ticketing.domain

import jakarta.persistence.*


@Entity
class Seat {

    var row : String? = null
    var column : Int? = null
    //좌석 선택

    @Enumerated(EnumType.STRING)
    // default : POSSIBLE
    var selectStatus : SelectStatus = SelectStatus.POSSIBLE // POSSIBLE, IMPOSSIBLE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    var theater : Theater? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    val id : Long = 0L

    protected constructor() {
    }

    constructor(row: String?, column: Int?, selectStatus: SelectStatus, theater: Theater?) {
        this.row = row
        this.column = column
        this.selectStatus = selectStatus
        this.theater = theater
    }
}
