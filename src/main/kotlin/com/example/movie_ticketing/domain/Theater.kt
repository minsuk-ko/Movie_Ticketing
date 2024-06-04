package com.example.movie_ticketing.domain

import jakarta.persistence.*

@Entity
class Theater {

    var name: String? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0


    override fun toString(): String {
        return "Theater(name=$name, id=$id)"
    }
}