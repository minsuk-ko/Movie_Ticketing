package com.example.movie_ticketing.service
import com.example.movie_ticketing.repository.MemberRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

//findbyEmail을통해서 userDetails 객체를 생성해서
// 이 객체로 로그인 시도하려고 하는 거임

@Service
class CustomUserDetailsService(val memberRepository: MemberRepository) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val member = memberRepository.findByEmail(email)
            .orElseThrow{ UsernameNotFoundException("User not found with email: $email")}
// ?: throw UsernameNotFoundException("User not found with email: $email") 이렇게 삼항연산자를 썻는데 안됨
        //Optional 타입에서 객체가 비어있을 때 지정한 예외 사용할 수 있게 처리함
        return User.builder()
            .username(member.email)
            .password(member.password)
            .authorities("ROLE_USER") //spring Security관례 기본적인 메소드가 ROLE_ 이렇게 설정
            // 그냥 USER해도 되나 hasAuthority 같은 메소드로 권한 검사해야함
            .build()
    }
}