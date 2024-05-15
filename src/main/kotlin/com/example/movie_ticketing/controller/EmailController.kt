package com.example.movie_ticketing.controller

import com.example.movie_ticketing.service.EmailService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController



@RestController
class EmailController(private val emailService: EmailService ) {


    @PostMapping("/send/mail")
    fun sendMail(@RequestParam email: String) {
        emailService.sendMail(email)
    }

}


//@Controller
//class EmailController(private val emailService: EmailService) {
//
//
//    @PostMapping("/infoCheck/sendEmail")
//    fun sendEmail(@RequestParam("email") email : String, session : HttpSession) : ResponseEntity<Void>{
//        // email 전송, password 저장
//        val verificationCode = emailService.sendVerificationCode(email)
//        // session 에 저장될 인증번호 저장
//        session.setAttribute("verificationCode", verificationCode)
//        return ResponseEntity.ok().build()
//    }
//
//    // 입력한 인증번호와 세션에 저장된 인증번호 비교
//    @PostMapping("/infoCheck/verifyCode")
//    fun verifyCode(@RequestParam("inputCode") inputCode : String, session : HttpSession) : ResponseEntity<Boolean>{
//        val storedCode = session.getAttribute("verificationCode") as? String
//
//        if(storedCode != null && storedCode == inputCode){
//            return ResponseEntity.ok(true)
//        } else{
//            return ResponseEntity.ok(false)
//        }
//    }
//
//    /**
//     * 비밀번호 찾기 이메일 보내기
//     * 막힘
//     */
//   // @PostMapping("/findPw")
//    //fun sendPasswordEmail(@RequestBody member : Member){
//    //    emailService
//   // }
//
//
//}