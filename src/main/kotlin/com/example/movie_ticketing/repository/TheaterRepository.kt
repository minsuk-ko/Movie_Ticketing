package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Theater
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@Repository
interface TheaterRepository :JpaRepository<Theater, Int> {
    fun existsByName(name:String) :Boolean
    fun findByName(name: String):Theater


}