package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.Member
import com.example.movie_ticketing.repository.MemberRepository
import com.example.movie_ticketing.repository.ReservationRepository
import com.example.movie_ticketing.repository.TicketRepository
import com.example.movie_ticketing.service.CustomUserDetailsService
import com.example.movie_ticketing.service.MemberService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping


@Controller
class AdminController(private val memberService: MemberService,
                      private val memberRepository: MemberRepository,
                      private val passwordEncoder: PasswordEncoder,
                      private val userDetailsService: CustomUserDetailsService,
                      private val ticketRepository: TicketRepository,
                      private val reservationRepository: ReservationRepository
) {
    //어드민 설정방법 Mariadb 들어가서 직접 설정하는 거로
    // update member set role = 'ROLE_ADMIN' where id =?;
    //+ 회원가입후 로그인 한 상태에서 저 명령문해서 바꿨다면 다시 로그인 해야함
    // 이미 ROLE_USER의 권한을 부여받은 상태에서 들어간 거라서 로그아웃 하고 재로그인하면 돼

    //securityconfig에서
    // 특정권한이 있을때만 들어갈 수 있게끔 설정함

    // 관리자 페이지의 회원 관리
    @GetMapping("/admin/member")
    // 한 페이지에 보여질 항목의 수 = 10
    fun memberManagement(@PageableDefault(size = 5) pageable: Pageable, model: Model): String {
        val page: Page<Member?> = memberRepository.findAll(pageable)
        model.addAttribute("membersPage", page)
        return "adminMember"
    }

    //임시 페이지 회원 누르면 여기링크 들어감
    @GetMapping("/admin/memberinfo/{id}")
    fun memberInfo(@PathVariable id :Int,model: Model) : String{
        val member = memberRepository.findById(id)

        model.addAttribute("member",member)

        return "adminMeberInfo"
    }

    /**
     * 관리자 페이지에서 회원 탈퇴시키기
     */
    @Transactional // 2개이상의 쿼리를 묶어서 db에 전송해야하므로 사용
    //만약 에러가 발생할 경우 자동으로 모든과정을 원래대로 되돌려 놓음
    @GetMapping("/admin/member/{id}")
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
        /* 예매내역삭제 이렇게 어때?*/
        val reservations = reservationRepository.findByMemberId(id) //멤버에 관련된 모든 예매내역 꺼내기


        //deleteByreservation => delete from tikets where reservation_id =?
        // 예약 정보에 따른 티켓들 삭제
        if(reservations.isNotEmpty()) {  //예매 내용이 있을경우 삭제
            reservations.forEach { reservation ->
                ticketRepository.deleteByReservation(reservation)
                reservationRepository.delete(reservation)
            }
        }
        //forEach: 리스트 각각의 요소에 순차적으로 처리시킴
        // 즉 ,티켓 삭제 메소드 실행후 예약 삭제 메소드

        memberRepository.deleteById(id)

        return "redirect:/admin/member"
    }


}