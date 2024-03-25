package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Member
import com.example.movie_ticketing.repository.MemberRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class MemberServiceTest{

    @Autowired lateinit var memberService : MemberService
    @Autowired lateinit var memberRepository: MemberRepository

    @Test
    fun join(){

        val member = Member("Test@naver.com","test",20)

        val savedId = memberService.join(member)

        assertEquals(member, memberRepository.findOne(savedId))
    }
}