//package com.example.movie_ticketing.config.util
//
//import com.example.movie_ticketing.service.EmailService
//import org.springframework.mail.SimpleMailMessage
//import org.springframework.mail.javamail.JavaMailSender
//import org.springframework.mail.javamail.MimeMessageHelper
//import org.springframework.stereotype.Component
//import java.util.*
//import javax.mail.internet.MimeMessage
//
//
//@Component
//class MailUtil(private val javaMailSender: JavaMailSender) {
//
//    fun sendMail(toEmail: String) {
//        val length = 6
//        val randomString = getRandomString(length)
//
//        val message = SimpleMailMessage()
//        message.setTo(toEmail)
//        message.setFrom("neko2nu.gmail.com")
//        message.setSubject("이메일 인증 이메일입니다.")
//        message.setText("인증 코드 : $randomString")
//
//        try {
//            javaMailSender.send(message)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun getRandomString(length: Int): String {
//        val uuid = UUID.randomUUID().toString()
//        return uuid.substring(0, length)
//    }
//}