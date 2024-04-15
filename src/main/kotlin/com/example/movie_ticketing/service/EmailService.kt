package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Member
import com.example.movie_ticketing.repository.MemberRepository
import logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.security.SecureRandom
import kotlin.random.Random

@Service
class EmailService @Autowired constructor(
    private val javaMailSender: JavaMailSender,
    private val memberRepository: MemberRepository
) {
    private val logger = LoggerFactory.getLogger(EmailService::class.java)

    fun sendVerificationCode(recipient: String): String {
        val verificationCode = generateEmailCode()
        val subject = "회원가입 인증 이메일 입니다."
        val message = "인증번호는 $verificationCode 입니다."

        sendEmail(recipient, subject, message)
        return verificationCode
    }

    fun sendTemporaryPassword(recipient: String, member: Member): String {
        val temporaryPassword = generatePassword()
        val subject = "임시 비밀번호 안내 이메일입니다."
        val message = "임시 비밀번호는 $temporaryPassword 입니다."

        sendEmail(recipient, subject, message)
        updatePassword(temporaryPassword, member)
        return temporaryPassword
    }

    fun sendEmail(recipient: String, subject: String, text: String) {
        try {
            val message = SimpleMailMessage().apply {
                setTo(recipient)
                setSubject(subject)
                setText(text)
            }
            javaMailSender.send(message)
            logger.info("이메일 전송 완료!")
        } catch (e: Exception) {
            logger.error("이메일 전송 실패: ", e)
        }
    }

    fun generateEmailCode(): String = SecureRandom().let { random ->
        val codeLength = 6
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        (0 until codeLength)
            .map { random.nextInt(alphabet.length) }
            .map(alphabet::get)
            .joinToString("")
    }

    fun updatePassword(newPassword: String, member: Member) {
        // Ideally, hash the password before storing it
        memberRepository.updatePassword(member.id, hashPassword(newPassword))
    }

    fun generatePassword(): String = SecureRandom().let { random ->
        val charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray()
        (0 until 10)
            .map { random.nextInt(charSet.size) }
            .map(charSet::get)
            .joinToString("")
    }

    private fun hashPassword(password: String): String {
        // Implement password hashing here
        return password // Placeholder for hashing logic
    }
}