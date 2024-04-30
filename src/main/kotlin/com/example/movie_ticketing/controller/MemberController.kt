package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.Member
import com.example.movie_ticketing.repository.MemberRepository
import com.example.movie_ticketing.service.MemberService
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

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

    @PreAuthorize("isAuthenticated()") //로그인 했을때
    @GetMapping("/mypage1")
    fun mypage(auth: Authentication): String {
        return "mypage1.html"
    }

    /**
     * 회원가입 페이지로 이동
     */
    @GetMapping("/join")
    fun createForm(): String {

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