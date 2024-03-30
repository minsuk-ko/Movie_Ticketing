package com.example.movie_ticketing.service

import com.example.movie_ticketing.dao.MemberDao
import com.example.movie_ticketing.domain.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class MemberService(val memberDao : MemberDao){

    /**
     * 회원가입
     */
    fun join(member : Member): Int {
        validateDuplicateEmail(member)
        memberDao.save(member)
        return member.id
    }

    /**
     * 중복 이메일 방지
     */
    private fun validateDuplicateEmail(member: Member) {
        val findEmail = memberDao.findByEmail(member.email)
        if (findEmail != null){
            throw IllegalStateException("이미 가입된 이메일입니다 :(")
        }
    }

    /**
     * 회원 한명 조회
     */
    fun findOne(memberId : Int) : Member?{
        return memberDao.findById(memberId);
    }
}