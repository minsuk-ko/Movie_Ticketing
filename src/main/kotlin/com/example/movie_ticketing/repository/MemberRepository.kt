package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface MemberRepository : JpaRepository<Member?, Int?> {
    // todo
//    fun findAll(pageable: Pageable): Page<Member?>

    fun findByEmail(email: String) : MutableList<Member>

    fun findByName(member: Member)

    fun findByNameAndEmail(name: String, email: String)

    fun existsByEmail(email: String); //email 존재여부(Db에 있으면 참)

    @Modifying
    @Transactional
    @Query("update Member set password=:password where id=:id")
    fun updatePassword(id: Int, password : String)

}
