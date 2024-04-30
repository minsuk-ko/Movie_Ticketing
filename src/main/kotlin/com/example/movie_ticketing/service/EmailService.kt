//package com.example.movie_ticketing.service
//
//import com.example.movie_ticketing.domain.Member
//import com.example.movie_ticketing.repository.MemberRepository
//import logger
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.beans.factory.annotation.Configurable
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.mail.SimpleMailMessage
//import org.springframework.mail.javamail.JavaMailSender
//import org.springframework.stereotype.Service
//import java.security.SecureRandom
//import kotlin.random.Random
//
//@Service
//class EmailService(val javaMailSender: JavaMailSender, val memberRepository: MemberRepository) {
//
//    /**
//     * 회원가입 이메일 인증
//     */
//    fun sendVerificationCode(recipient : String) : String{
//        val verificationCode : String = generateEmailCode()
//        val subject = "회원가입 인증 이메일 입니다."
//        val message = "인증번호는 $verificationCode 입니다."
//
//        sendEmail(recipient, subject, message)
//        return verificationCode // 세션에 저장
//    }
//
//    /**
//     * 비밀번호 찾기 이메일
//     * todo 여기 하는중인데 뭔가 이상함
//     */
//    fun sendTemporaryPassword(recipient : String, member : Member) : String {
//        val temporaryPassword : String = generatePassword()
//        val subject = "임시 비밀번호 안내 이메일입니다."
//        val message = "임시 비밀번호는 $temporaryPassword 입니다."
//
//        sendEmail(recipient, subject, message)
//        updatePassword(temporaryPassword,member)
//        return temporaryPassword // 세션에 저장
//    }
//
//    fun sendEmail(recipient : String, subject : String, text : String){
//        val message = SimpleMailMessage().apply {
//            setTo(recipient)
//            setSubject(subject)
//            setText(text)
//        }
//        javaMailSender.send(message)
//        logger().info("이메일 전송 완료!")
//    }
//
//    /**
//     * 인증번호 생성
//     * @return 6자리 random alphabet
//     */
//    fun generateEmailCode() : String{
//
//        val codeLength : Int = 6
//        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
//        val sb = StringBuilder(codeLength)
//        val random = SecureRandom()
//
//        for(i in 0 until codeLength){
//            val index = random.nextInt(alphabet.length)
//            val randomChar = alphabet[index]
//            sb.append(randomChar)
//        }
//        return sb.toString()
//    }
//
//    /**
//     * 비밀번호 변경
//     */
//    fun updatePassword(str : String, member : Member){
//        memberRepository.updatePassword(member.id, str)
//    }
//
//    /**
//     * 비밀번호 생성
//     * @return 10자리 랜덤난수
//     */
//    fun generatePassword() : String {
//        val charSet = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
//            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
//
//        return(0 until 10)
//            .map { Random.nextInt(charSet.size) }
//            .map { charSet[it] }
//            .joinToString { "" }
//    }
//
//}