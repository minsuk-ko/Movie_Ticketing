package com.example.movie_ticketing.dao

import com.example.movie_ticketing.Page
import com.example.movie_ticketing.domain.Member

interface MemberDao {
    fun list(page: Page): List<Member>

    fun getById(id: Int): Member?

    fun getByMemberEmail(email: String): Member?

    fun join(member: Member): Member?

    fun changePassword(id: Int, password: String): Int

    fun deleteById(id: Int): Int
}