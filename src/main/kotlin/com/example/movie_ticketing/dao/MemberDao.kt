//package com.example.movie_ticketing.dao
//
//import com.example.movie_ticketing.Page
//import com.example.movie_ticketing.domain.Member
//
//interface MemberDao {
//    fun list(page: Page): List<Member>
//
//    fun findById(id: Int): Member?
//
//    fun findByEmail(email: String): Member?
//
//    fun save(member: Member): Member?
//
//    fun changePassword(id: Int, password: String): Int
//
//    fun deleteById(id: Int): Int
//}