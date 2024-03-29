package com.example.movie_ticketing.domain

import jakarta.persistence.Id
import java.time.LocalDate

data class Reservation(@Id var id: Int = 0,
                  var date : LocalDate,
                  var price : Int =0,
                  var ticketNum : Int = 0,
                  var member : Member){

}