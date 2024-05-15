package com.example.movie_ticketing.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap


@Service
class EmailService(private val javaMailSender: JavaMailSender) {

    private val verificationCodes: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    fun verifyCode(email: String, code: String): Boolean {
        return verificationCodes[email] == code
    }

    fun sendMail(toEmail: String) {
        val length = 6
        val randomString = getRandomString(length)
        verificationCodes[toEmail] = randomString

        val message = SimpleMailMessage()
        message.setTo(toEmail)
        message.setFrom("neko2nu.gmail.com")
        message.setSubject("이메일 인증 이메일입니다.")
        message.setText("인증 코드 : $randomString")

        try {
            javaMailSender.send(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getRandomString(length: Int): String {
        val uuid = UUID.randomUUID().toString()
        return uuid.substring(0, length)
    }
}