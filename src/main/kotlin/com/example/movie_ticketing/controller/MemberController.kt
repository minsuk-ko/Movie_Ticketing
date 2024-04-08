package com.example.movie_ticketing.controller

import com.example.movie_ticketing.domain.Member
import com.example.movie_ticketing.service.MemberService
import jakarta.validation.Valid
import logger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class MemberController(private val memberService: MemberService) {

    /**
     * 회원가입 페이지로 이동
     */
    @GetMapping("/join")
    fun createForm(model: Model): String {
        model.addAttribute("memberForm",MemberForm())
        return "createMemberForm"
    }

    @PostMapping("/join")
    fun create(@Valid form: MemberForm, result : BindingResult) : String{
        if(result.hasErrors()){
            return "createMemberForm"
        }

        if(form.password != form.confirmPassword){
            result.rejectValue("confirmPassword","passwordInCorrect",
                "패스워드가 일치하지 않습니다.")
            return "createMemberForm"
        }

            // member 객체 생성
            val member = Member().apply {
                name = form.name
                age = form.age
                email = form.email
                password = form.password
            }
            memberService.join(member)

        return "redirect:/"
    }
}