package com.example.movie_ticketing.service

import com.example.movie_ticketing.repository.MemberRepository
import com.example.movie_ticketing.domain.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class MemberService(var memberRepository: MemberRepository){

    /**
     * 회원가입
     */
    fun join(member: Member): Int{
        validateDuplicateEmail(member)
        memberRepository.save(member)
        return member.id
    }

    /**
     * 이메일 중복
     */
    private fun validateDuplicateEmail(member: Member) {
        val findEmail = memberRepository.findByEmail(member.email.toString())
        if(findEmail.isPresent){
            throw IllegalStateException("이미 가입된 이메일입니다 :(")
        }
    }
}