package com.example.movie_ticketing.domain

import jakarta.persistence.*

data class Schedule(@Id var id : Int =0,
                    var start : String = "",
                    var end : String = "",
                    var date : String = "",
                    var movie : Movie,
                    var theater: Theater) {
}