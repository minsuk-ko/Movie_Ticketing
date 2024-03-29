package com.example.movie_ticketing.domain

import jakarta.persistence.Id

data class Ticket(@Id var id : Int = 0,
                  var price : Int =0,
                  var schedule: Schedule,
                  var seat: Seat,
                  var reservation: Reservation) {
}