package com.example.movie_ticketing.domain

import jakarta.persistence.*

data class Seat(@Id var id: Int=0,
           var row : String = "",
           var col : Int=0,
           var theater: Theater,
           var isSelected : Boolean) {
}
