package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.Member
import com.example.movie_ticketing.domain.Ticket
import com.example.movie_ticketing.repository.*
import com.example.movie_ticketing.service.ScheduleService
import com.example.movie_ticketing.service.TicketService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Controller
class AdminController(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val ticketRepository: TicketRepository,
    private val reservationRepository: ReservationRepository,
    private val scheduleService: ScheduleService,
    private val ticketService: TicketService,
    private val movieRepository: MovieRepository,
    private val scheduleRepository: ScheduleRepository
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
        val member = memberRepository.findById(id).orElseThrow { IllegalArgumentException("Invalid member Id: $id") }

        model.addAttribute("member",member)

        return "adminMemberInfo"
    }
    @GetMapping("/admin/memberinfo2/{id}")
    fun memberInfo2(@PathVariable id :Int,model: Model) : String{
        val member = memberRepository.findById(id)
        if(member.isPresent)
        { val reservations =reservationRepository.findByMemberId(id)

            val reservationTicketsMap = reservations.map { reservation ->
                val tickets = ticketRepository.findByReservation(reservation)
                reservation to tickets
            }.toMap() //Map<Reservation, List<Ticket>> 꼴로 변환시킴 즉 키가 reservation,값이 ticket

            println(reservationTicketsMap)


            model.addAttribute("reservations", reservations)
            model.addAttribute("reservationTicketsMap", reservationTicketsMap)
            model.addAttribute("currentDate", LocalDate.now())
            model.addAttribute("member", member.get()) // 멤버 id쉽게꺼낼려고

            // 각 티켓의 상영 시간을 'HH:mm' 형식의 문자열로 변환하여 모델에 추가
            val formattedTimesMap = reservationTicketsMap.mapValues { entry ->
                entry.value.map { ticket ->
                    val formattedStartTime = ticket.schedule.start.format(DateTimeFormatter.ofPattern("HH:mm"))
                    ticket to formattedStartTime
                }.toMap()
            }  //안그러면 10:00:00이런 방식으로 뜸
            model.addAttribute("formattedTimesMap", formattedTimesMap)
        }else{
            return "/admin/member"}
        return "adminMemberInfo2"
    }

    @GetMapping("admin/adminTheater")
    fun redirectToDefaultTheater(): String {
        return "redirect:/admin/adminTheater/1"
    }

    @GetMapping("admin/adminTheater/{id}")
    fun getAdminTheater(@PathVariable id: Int, model: Model): String {
        val schedules = scheduleService.getSchedulesByTheaterId(id)
        model.addAttribute("schedules", schedules)
        model.addAttribute("theaterId", id)

        val tickets = ticketService.getTicketsByTheaterId(id)
        val groupedTickets = tickets.groupBy { it.schedule.id }
        val safeGroupedTickets = groupedTickets ?: emptyMap<Int, List<Ticket>>()

        model.addAttribute("groupedTickets", safeGroupedTickets)
        return "adminTheater"
    }


    /**
     * 관리자 페이지에서 회원 탈퇴시키기
     */
    @Transactional // 2개이상의 쿼리를 묶어서 db에 전송해야하므로 사용
    //만약 에러가 발생할 경우 자동으로 모든과정을 원래대로 되돌려 놓음
    @GetMapping("/admin/member/delete/{id}")
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

  @PostMapping("/admin/memberinfo/change-password")
  fun change(@RequestParam password:String,
             @RequestParam id:Int,
             redirectAttributes:RedirectAttributes):String{

      val member=memberRepository.findById(id)
    //멤버 상세보기 버튼을 눌러서 들어가므로 직접 url에 치지않는 이상 null이 아님
      if(member.isPresent) {
          val newPw = passwordEncoder.encode(password)


          memberRepository.updatePassword(id,newPw)//어차피 멤버 id랑 같은 id
          redirectAttributes.addAttribute("id", id)
          return "redirect:/admin/memberinfo/{id}"
      }
      return "redirect:/admin/member"

  }






    @Transactional
    @PostMapping("/admin/cancelReservation")
    fun cancelReservation(@RequestParam reservationId: Int, @RequestParam memberId: Int): String {
        val reservation = reservationRepository.findById(reservationId)
        if (reservation.isPresent) {
            val tickets = ticketRepository.findByReservationId(reservationId)
            tickets.forEach { ticket ->
                ticketRepository.delete(ticket)
            }
            reservationRepository.deleteById(reservationId)
        }
        return "redirect:/admin/memberinfo2/$memberId"
    }

    @GetMapping("/admin/adminMovie")
    fun getMovies(model: Model): String {
        val movies = movieRepository.findAll()
        val moviesWithScheduleStatus = movies.associate { movie ->
            val hasSchedule = scheduleRepository.existsByMovieId(movie!!.id)
            movie to hasSchedule
        }
        model.addAttribute("moviesWithScheduleStatus", moviesWithScheduleStatus)
        return "adminMovie"
    }



}
