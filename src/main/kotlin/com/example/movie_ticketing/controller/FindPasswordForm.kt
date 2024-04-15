package com.example.movie_ticketing.controller

import jakarta.validation.constraints.NotEmpty

class FindPasswordForm {

    @NotEmpty(message = "이름을 입력해주세요.")
    var name : String = ""

    @NotEmpty(message = "이메일을 입력해주세요.")
    var email : String = ""
}