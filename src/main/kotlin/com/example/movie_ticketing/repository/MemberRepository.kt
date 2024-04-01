package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member?, Int?> {
    // todo
    override fun findAll(pageable: Pageable): Page<Member?>

    fun findByEmail(member: Member) : MutableList<Member>

}
