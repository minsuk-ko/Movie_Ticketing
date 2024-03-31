package com.example.movie_ticketing.domain

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import org.springframework.data.annotation.Id


data class Member (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Int =0,
    var name : String ="",
    var email: String ="",
    var age: Int =0,
    var password : String =""){

}