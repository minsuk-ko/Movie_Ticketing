package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.Member
import com.example.movie_ticketing.repository.MemberRepository
import com.example.movie_ticketing.repository.TicketRepository
import com.example.movie_ticketing.service.CustomUserDetailsService
import com.example.movie_ticketing.service.MemberService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping


@Controller
class AdminController(private val memberService: MemberService,
                      private val memberRepository: MemberRepository,
                      private val passwordEncoder: PasswordEncoder,
                      private val userDetailsService: CustomUserDetailsService,
                      private val ticketRepository: TicketRepository
) {

    // 관리자 페이지의 회원 관리
    @GetMapping("/admin/member")
    // 한 페이지에 보여질 항목의 수 = 10
    fun memberManagement(@PageableDefault(size = 5) pageable: Pageable, model: Model): String {
        val page: Page<Member?> = memberRepository.findAll(pageable)
        model.addAttribute("membersPage", page)
        return "adminMember"
    }

    /**
     * 관리자 페이지에서 회원 탈퇴시키기
     */
    @PostMapping("/admin/member/{id}")
    fun deleteMember(@PathVariable id : Int) : String {

        /**
         * 회원이 예약했던 정보가 있다면(회원이 티켓을 가지고 있었다면)
         * 티켓을 전부 삭제해야 한다.
         * 원래는 ReservationRepository.deleteByMember(member) 를 하면 되지만
         * ReservationRepository 를 안쓰고 TicketRepository.save 를 사용해 바로 티켓을 만들었기 때문에
         * member 가 예약한 티켓을 TicketRepository 에서 찾아야 한다.
         * 어떻게 찾음??
         */
        // 삭제한 회원에 대한 예약 정보가 있다면 삭제
        // ticketRepository.delete(ticket)

        // 회원 삭제
        memberRepository.deleteById(id)

        return "redirect:/admin/member"
    }


}