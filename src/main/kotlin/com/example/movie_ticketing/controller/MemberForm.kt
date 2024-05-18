package com.example.movie_ticketing.controller

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull


class MemberForm {

    var name: String = ""

    @Email(message = "이메일 형식을 맞춰주세요")
    var email: String = ""

    var age: Int? = 0

    @NotEmpty(message = "비밀번호를 입력해주세요")
    var password: String = ""

    @NotEmpty(message = "비밀번호 확인은 필수입니다")
    var confirmPassword: String = ""

    @NotEmpty(message = "인증 코드를 입력해주세요")
    var emailCode: String = ""

    override fun toString(): String {
        return "MemberForm(name='$name', email='$email', age=$age, password='$password', confirmPassword='$confirmPassword', emailCode='$emailCode')"
    }
}