package com.example.movie_ticketing.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
class Theater {

    var name : String? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    val id : Long = 0L

    protected constructor() {
    }

    constructor(name: String?) {
        this.name = name
    }


}
