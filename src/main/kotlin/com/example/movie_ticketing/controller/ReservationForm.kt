package com.example.movie_ticketing.controller

class ReservationForm {

    // 선택한 영화를 Int 형식(movieId)으로 저장
    var movieId : Int = 0

    // 선택한 날짜를 LocalDate 타입이 아닌 String 형식(ex : 2024:05:03)으로 저장
    var date : String = ""

    // 선택한 시간을 String 형식(ex : 10:30) 으로 저장
    var time : String = ""

    // 선택한 좌석 Id 목록
    var seatIds : List<Int> = listOf()

    override fun toString(): String {
        return "ReservationForm(movieId=$movieId, date='$date', time='$time', seatIds=$seatIds)"
    }
}