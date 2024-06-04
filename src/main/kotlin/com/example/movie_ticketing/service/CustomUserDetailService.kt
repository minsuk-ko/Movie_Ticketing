package com.example.movie_ticketing.service
import com.example.movie_ticketing.repository.MemberRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

//findbyEmail을통해서 userDetails 객체를 생성해서
// 이 객체로 로그인 시도하려고 하는 거임

//시큐리티 설정에서 loginProcessUrl("/login")설정했기에
//로그인 요청이 오면 자동으로 UserDetailService 타입으로 Ioc되어 있는 loadUserByUsername이 실행
// 규칙임 ->정의
//UserDetailServiece는 결국 Authentication 객체 생성위해서 커스터마이징 한 것
//시큐리티 session(내부 Authentication(내부 UserDetails))

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
            .authorities(member.role) // 이 role권한을 멤버에 저장되어 있는 것을 확인해서 가져감
            // 그렇기에 권한을 바꾸려면
            //  마리아db들어가서 update문을 사용해야함
            .build()

    }
}