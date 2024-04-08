package com.example.movie_ticketing.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
interface EmailService {

    val javaMailSender: JavaMailSender

    fun sendVerificationCode(recipient : String) : String{
        val verificationCode : String = generateEmailCode()
        val subject = "회원가입 인증 이메일 입니다."
        val message = "인증번호는 $verificationCode 입니다."

        sendEmail(recipient, subject, message)
        return verificationCode // 세션에 저장
    }

    fun sendEmail(recipient : String, subject : String, text : String){
        val message = SimpleMailMessage().apply {
            setTo(recipient)
            setSubject(subject)
            setText(text)
        }
        javaMailSender.send(message)
    }

    /**
     * 인증번호 생성
     * @return 6자리 random alphabet
     */
    fun generateEmailCode() : String{

        var codeLength : Int = 6
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        val sb = StringBuilder(codeLength)
        val random = SecureRandom()

        for(i in 0 until codeLength){
            val index = random.nextInt(alphabet.length)
            val randomChar = alphabet[index]
            sb.append(randomChar)
        }
        return sb.toString()
    }
}