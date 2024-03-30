package com.example.movie_ticketing.controller

import com.example.movie_ticketing.logger
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class HomeController {

    val log = logger()

    @RequestMapping("/")
    fun home(): String {
        log.info("home controller")
        return "home"
    }
}