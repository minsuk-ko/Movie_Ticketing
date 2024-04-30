package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.Member
import com.example.movie_ticketing.repository.MemberRepository
import com.example.movie_ticketing.service.MemberService
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.security.Principal

@Controller
class MemberController(

    private val memberService: MemberService,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder) {
    /**
     * 멤버 로그인
     * 조금 더 수정해야함
     */
    //Springsecurity 기본 로그인/로그아웃 경로
    //
    @GetMapping("/login")
    fun login(): String = "login.html"

    @GetMapping("/logout")  //get요청시 자동 로그아웃 +메인페이지로
    fun logout(): String {
        return "redirect:/"
    }
    /**
     * 마이페이지 (내정보 / 비밀번호 변경 / 탈퇴)
     */
    @PreAuthorize("isAuthenticated()") //로그인 했을때
    @GetMapping("/mypage1")
    fun mypage(auth: Authentication,model: Model): String {
        // Authentication 객체에서 UserDetails를 추출하고
        // 이를 통해 username (이메일)을 얻음 기본적으로 Spring security
        val userDetails = auth.principal as UserDetails
        //Authentication객체는 스프링 시큐리티에서 인증된(로그인된)사용자 상세정보 포함됨
        // Authentication은 UserDetails를 구현한 객체인데
        //UserDetails는 사용자 정보를 캡슐화한 인터페이스
        val email = userDetails.username
        val member = memberRepository.findByEmail(email)//  Optional은 따로 wrapper 클래스 => findbyemail로 꺼내갔을 때는 optional타입이므로
            // .orElseThrow로 타입검증?그렇게 생각하면 될듯
            .orElseThrow { IllegalArgumentException("No member found with email: $email") }
        // member객체가 Optional타입이라서 orElseThrow를 통해 실제 값에 접근해야한다고함
        //  null일 가능성이 있어서 member객체를 추출하지 못했어

        model.addAttribute("member",member)


        return "mypage1.html"
    }

    @PostMapping("/update-password")
    fun changePw(auth: Authentication,@RequestParam password:String) :String{
        //RequestParam 을이용해서 form태그에서 제출한 것중 name이 password인것을 가져옴
        //패스워드 변수에 저장되어있기에 업데이트 하거나 저장하면될듯?
        // UserDetails를 바로 파라미터로 받고 하려고했는데 UserDetails는 인터페이스여서 불가
        // principal은 userdetails를 구현한 객체!!
        val newPw=passwordEncoder.encode(password)
        val userDetails = auth.principal as UserDetails
        val email = userDetails.username
        val member = memberRepository.findByEmail(email)
            .orElseThrow()
        println("이메일로 멤버 찾기")
        memberRepository.updatePassword(member.id,newPw)
        println("비밀번호 업데이트 완료")

        //return "mypage1"
    //return으로 할 경우 뷰를 직접 반환하는데 폼 제출이 재실행 될 수도 있음
        //즉 동일한 데이터로 여러번 비밀번호 고침이나 그런게  실행될 수 있음
        //redirect로 할 경우 폼제출과 관련된 데이터는 리디렉션 과정에서 사라지므로
        // 사용자가 새로고침해도 폼 제출이 다시 발생하지 않음
        //그래서 데이터 변경이나 그런걸 처리한 후에는 redirect로

        return "redirect:/mypage1"


    }

    @PostMapping("delete-member")
    fun delete(@RequestParam password: String, auth: Authentication):String{
        val userDetails =auth.principal as UserDetails
        val email = userDetails.username
        val member = memberRepository.findByEmail(email)
            .orElseThrow()
        // 비밀번호 확인
        if (!passwordEncoder.matches(password, member.password)) {
            // 비밀번호가 일치하지 않는 경우
           // 다시 본래페이지로
            return "redirect:/mypage1"
        }


        memberRepository.deleteById(member.id)
        // Spring Security 세션 정보 클리어
        // 세션이 로그인한 사용자의 상세정보,권한등등을 지워서 로그아웃
        SecurityContextHolder.clearContext()

        return "redirect:/logout"
    }



    /**
     * 회원가입 페이지로 이동
     */
    @GetMapping("/join")
    fun createForm(model: Model): String {

        return "join"
    }

    @PostMapping("/addmember") //따로 페이지 안만들어도 됨.
    fun create(@Valid form: MemberForm, result: BindingResult,redirectAttributes: RedirectAttributes): String {
        println(form)

        if (result.hasErrors()) {
            return "redirect:/join";
        }
        println(form)
        if (form.password != form.confirmPassword) {
            result.rejectValue(
                "confirmPassword", "passwordInCorrect",
                "패스워드가 일치하지 않습니다."
            )
            return "redirect:/join";
        }

        val hashpassword = passwordEncoder.encode(form.password)
        // member 객체 생성
        val member = Member().apply {
            name = form.name
            age = form.age
            email = form.email
            password = hashpassword
        }
        memberService.join(member)
        //model.addAttribute => 컨트롤러에서 데이터 처리한 것을 view로 전달하는데 사용
        //즉 뷰에서 사용할 데이터를 모델 객체에 추가하는 역할
        // 현재 요청의 모델 객체에 데이터 추가 => 현재 진행중인 요청에서 데이터를 뷰에 전달할때
        // 사용: 리다이렉트 없이 뷰를 직접 반환하는 경우에 사용됨 (즉, 입력 받아서 처리후 에러메시지를 표시하고 동일페이지 보여줄때)
        // 리다이렉트를 실행할 경우 이거로 추가된 데이터는 새페이지로 전달 안됨
      //  model.addAttribute("member", member)


       redirectAttributes.addFlashAttribute("member", member)  // Redirect 후에도 데이터 유지
        //redirectAttributes.addFlashAttribute() : 데이터를 Flash attribute로 저장하여 리다이렉트 후에도 일회성으로 데이터를 사용할 수 있게 한다.
        // 일회성이라 추가된 데이터는 리다이렉트된 페이지가 로드 될 때까지만 유지, 페이지 떠나면 사라짐
        // 사용 : 데이터 포함한 상태에서 리다이렉트 해야할 때 사용
        // 리다이렉트 하는 동안 데이터 유지돼서, 사용자가 브라우저에서 새로고침 해도 데이터가 중복해서 전송되는 것을 방지

        return "redirect:/joinComplete"  // redirect: 라는 지시를 내리는이유
        // 브라우저에게 새로운 페이지로 완전히 이동하라는 지시를 내림
        // -> URL이 변경되고 새로운 페이지 요청

        // 그냥 return "/joinComplete"라고 하면 뷰이름을 반환하는 거임
        // 무슨 말이냐? 해당 뷰 이름과 일치하는 뷰템플릿 => html을 찾아서 이 페이지로 연결됨
        // 그래서 /addmember URL에서 /joinComplete 페이지가 나오는 것임
    }
    @GetMapping("/joinComplete")
    fun joinCom(model: Model):String{
        // 리다이렉트에서 전달된 'member' 객체를 모델에 추가하지 않아도 됨
        // Flash attribute로 추가된 'member'는 한 번만 사용될 수 있음
        return "joinComplete"
    }
    /**
     * 비밀번호 찾기
     * 해당 이메일이 DB에 있는지 확인 후 임시 비밀번호 전송
     * 임시 비밀번호를 현재 비밀번호로 변경
     */
    @GetMapping("/findPw")
    fun findPwForm(model : Model) : String {
        model.addAttribute("findPasswordForm", FindPasswordForm())
        return "createFindPasswordForm"
    }

    @PostMapping("/findPw")
    fun findPw(@Valid form : FindPasswordForm, result: BindingResult) : String{
        if(result.hasErrors()){
            return "createFindPasswordForm"
        }

        // todo 일단 pass...
        return ""
    }
}