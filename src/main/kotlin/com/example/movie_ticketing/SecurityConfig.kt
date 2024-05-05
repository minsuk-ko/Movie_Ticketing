package com.example.movie_ticketing

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean  //비밀번호 암호화 메소드 정의
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }



    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() } //csrf토큰 비활성화
        http.authorizeHttpRequests { authorize ->
            authorize.requestMatchers("/**").permitAll()
            //모든 url 로그인 없이 허용
        }
        http.formLogin { formLogin ->
            formLogin.loginPage("/login") //로그인 페이지
                .usernameParameter("email") //username이 원래 로그인할때 쓰는 필드?
                .failureUrl("/login?error=true") //로그인 실패시 url
                .defaultSuccessUrl("/") //로그인 성공시 메인페이지
        }
        http.logout { logout ->
            logout.logoutUrl("/logout") //로그아웃 url
                .logoutSuccessUrl("/") //로그아웃성공시 이동할url
        }
        return http.build()
    }
}
