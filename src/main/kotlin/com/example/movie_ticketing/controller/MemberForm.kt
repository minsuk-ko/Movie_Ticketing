package com.example.movie_ticketing.controller

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull


class MemberForm {

    @NotEmpty(message = "이름을 입력해주세요.")
    var name : String = ""

    @NotEmpty(message = "이메일을 입력해주세요.")
    var email : String = ""

    @NotNull(message = "나이를 입력해주세요")
    var age : Int? = 0

    @NotEmpty(message = "비밀번호를 입력해주세요")
    var password : String = ""

    @NotEmpty(message = "비밀번호 확인은 필수입니다")
    var confirmPassword : String = ""

    override fun toString(): String {
        return "MemberForm(name='$name', email='$email', age=$age, password='$password', confirmPassword='$confirmPassword')"
    }
}