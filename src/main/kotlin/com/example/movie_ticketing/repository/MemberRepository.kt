package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long?> {

    fun findByEmail(email : String) : MutableList<Member>

    fun findOne(id : Int) : Member
}

// 비밀번호 변경
interface CustomMemberRepository {
    fun changePassword(id: Int, password: String)
}

class CustomUserRepositoryImpl(private val npjo: NamedParameterJdbcOperations) : CustomMemberRepository {

    companion object {
        private const val CHANGE_PASSWORD =
            "update member set password=:password where id=:id"
    }

    override fun changePassword(id: Int, password: String) {
        npjo.update(CHANGE_PASSWORD, mapOf("id" to id, "password" to password))
    }
}
