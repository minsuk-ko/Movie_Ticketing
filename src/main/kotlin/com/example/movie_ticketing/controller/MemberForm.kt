package com.example.movie_ticketing.controller

import jakarta.validation.constraints.NotEmpty


class MemberForm {

    @NotEmpty(message = "이름을 입력해주세요.")
    var name : String = ""

    @NotEmpty(message = "이메일을 입력해주세요.")
    var email : String = ""

    @NotEmpty(message = "나이를 입력해주세요")
    var age : Int? = null

    @NotEmpty(message = "비밀번호를 입력해주세요")
    var password : String = ""

}