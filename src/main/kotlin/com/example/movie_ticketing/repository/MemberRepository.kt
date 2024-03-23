package com.example.movie_ticketing.repository

import com.example.movie_ticketing.domain.Member
import org.springframework.data.repository.CrudRepository

interface MemberRepository : CrudRepository<Member?, Long?> { 
}