package com.example.movie_ticketing.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap


@Service
class EmailService(private val javaMailSender: JavaMailSender) {

    /**  ConcurrentHashMap : 이메일 주소와 해당 이메일 주소에 대한 인증 코드를 매핑하는 verificationCodes라는 이름의 변수를 선언한다
     *   ConcurrentHashMap : 다중 스레드 환경에서도 안전하게 이메일 주소와 인증 코드를 저장하고 접근할 수 있음 */
    private val verificationCodes: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    fun verifyCode(email: String, code: String): Boolean {
        return verificationCodes[email] == code
    }

    // 난수를 받아 메일 보내기
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

    // 랜덤 난수 생성
    private fun getRandomString(length: Int): String {
        val uuid = UUID.randomUUID().toString()
        return uuid.substring(0, length)
    }
}