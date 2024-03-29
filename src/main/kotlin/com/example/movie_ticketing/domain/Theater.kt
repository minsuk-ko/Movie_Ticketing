package com.example.movie_ticketing.domain

import jakarta.persistence.Id

data class Theater(@Id var id : Int =0,
                   var name : String = "") {

}
