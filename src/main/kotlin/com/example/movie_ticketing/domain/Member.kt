package com.example.movie_ticketing.domain

import org.springframework.data.annotation.Id


data class Member (
    @Id var id : Int =0,
    var name : String ="",
    var email: String ="",
    var age: Int =0,
    var password : String =""){

}