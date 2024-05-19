package com.example.movie_ticketing.controller

import com.example.movie_ticketing.service.EmailService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController



@RestController
class EmailController(private val emailService: EmailService) {

    @PostMapping("/send/mail")
    fun sendMail(@RequestParam email: String) {
        emailService.sendMail(email)
    }

    @PostMapping("/verify-code")
    fun verifyCode(@RequestBody request: VerifyCodeRequest): ResponseEntity<Map<String, Boolean>> {
        // 요청 본문(Body)에서 전달된 이메일 주소와 코드를 사용하여,
        // emailService의 verifyCode 메소드를 호출하여 코드의 유효성을 검사
        val isValid = emailService.verifyCode(request.email, request.code)

        // 검사 결과를 Map 객체에 담아 ResponseEntity 객체를 통해 클라이언트에게 JSON 형태로 응답
        // 이 때, "valid"라는 키를 사용하여 유효성 검사 결과를 전달함
        return ResponseEntity.ok(mapOf("valid" to isValid))
    }
}

data class VerifyCodeRequest(val email: String, val code: String)