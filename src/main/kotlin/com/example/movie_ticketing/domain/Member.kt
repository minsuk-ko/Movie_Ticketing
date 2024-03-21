package com.example.movie_ticketing.domain

import jakarta.persistence.*

@Entity
class Member {
    val email: String? = null
    val password: String? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    val id: Long = 0L
}