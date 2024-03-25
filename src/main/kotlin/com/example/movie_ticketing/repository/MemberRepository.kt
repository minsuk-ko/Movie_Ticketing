package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MemberRepository : JpaRepository<Member?, Long?> {

    @Query("select m from Member m where m.email = :email")
    fun findByEmail(email : String) : MutableList<Member>

    @Query("select m from Member m where m.id = :id")
    fun findOne(id : Long) : Member
}