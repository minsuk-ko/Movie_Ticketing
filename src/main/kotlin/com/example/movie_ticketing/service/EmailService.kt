package com.example.movie_ticketing.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap


@Service
class EmailService(private val javaMailSender: JavaMailSender) {

    private val verificationCodes: ConcurrentHashMap<String, String> = ConcurrentHashMap()

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

    fun verifyCode(email: String, code: String): Boolean {
        return verificationCodes[email] == code
    }
}


//@Service
//class EmailService (
//    private val javaMailSender: JavaMailSender,
//    private val memberRepository: MemberRepository
//) {
//    private val logger = LoggerFactory.getLogger(EmailService::class.java)
//
//    fun sendVerificationCode(recipient: String): String {
//        val verificationCode = generateEmailCode()
//        val subject = "회원가입 인증 이메일 입니다."
//        val message = "인증번호는 $verificationCode 입니다."
//
//        sendEmail(recipient, subject, message)
//        return verificationCode
//    }
//
//    fun sendTemporaryPassword(recipient: String, member: Member): String {
//        val temporaryPassword = generatePassword()
//        val subject = "임시 비밀번호 안내 이메일입니다."
//        val message = "임시 비밀번호는 $temporaryPassword 입니다."
//
//        sendEmail(recipient, subject, message)
//        updatePassword(temporaryPassword, member)
//        return temporaryPassword
//    }
//
//    fun sendEmail(recipient: String, subject: String, text: String) {
//        try {
//            val message = SimpleMailMessage().apply {
//                setTo(recipient)
//                setSubject(subject)
//                setText(text)
//            }
//            javaMailSender.send(message)
//            logger.info("이메일 전송 완료!")
//        } catch (e: Exception) {
//            logger.error("이메일 전송 실패: ", e)
//        }
//    }
//
//    fun generateEmailCode(): String = SecureRandom().let { random ->
//        val codeLength = 6
//        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
//        (0 until codeLength)
//            .map { random.nextInt(alphabet.length) }
//            .map(alphabet::get)
//            .joinToString("")
//    }
//
//    fun updatePassword(newPassword: String, member: Member) {
//        // Ideally, hash the password before storing it
//        memberRepository.updatePassword(member.id, hashPassword(newPassword))
//    }
//
//    fun generatePassword(): String = SecureRandom().let { random ->
//        val charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray()
//        (0 until 10)
//            .map { random.nextInt(charSet.size) }
//            .map(charSet::get)
//            .joinToString("")
//    }
//
//    private fun hashPassword(password: String): String {
//        // Implement password hashing here
//        return password // Placeholder for hashing logic
//    }
//}
