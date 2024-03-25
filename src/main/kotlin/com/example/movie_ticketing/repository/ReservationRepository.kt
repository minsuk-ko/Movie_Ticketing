//package com.example.movie_ticketing.repository
//
//import com.example.movie_ticketing.domain.Reservation
//import org.springframework.data.jpa.repository.JpaRepository
//import org.springframework.data.jpa.repository.Query
//
//interface ReservationRepository : JpaRepository<Reservation?, Long?> {
//
//    @Query("select m from Reservation m where m.id = :id")
//    fun findOne(id : Long) : Reservation
//}ㅍㄱ