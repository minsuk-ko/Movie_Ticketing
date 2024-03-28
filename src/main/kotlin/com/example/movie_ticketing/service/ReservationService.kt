//package com.example.movie_ticketing.service
//
//import com.example.movie_ticketing.domain.Reservation
//import com.example.movie_ticketing.repository.ReservationRepository
//import org.springframework.stereotype.Service
//import org.springframework.transaction.annotation.Transactional
//
//
//@Service
//@Transactional
//class ReservationService(var reservationRepository: ReservationRepository) {
//
//    fun saveReservation(reservation: Reservation) {
//        reservationRepository.save(reservation)
//    }
//
//    fun findAllReservation() : MutableList<Reservation?> {
//        return reservationRepository.findAll();
//    }
//
//    fun findOne(reservationId : Long): Reservation {
//        return reservationRepository.findOne(reservationId)
//    }
//} //폐기