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
        val isValid = emailService.verifyCode(request.email, request.code)
        return ResponseEntity.ok(mapOf("valid" to isValid))
    }
}

data class VerifyCodeRequest(val email: String, val code: String)