package com.example.movie_ticketing.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import springfox.documentation.service.ApiKey
@Component
class ApiConfiguration {
    @Value("\${tmdb.api.key}")
    lateinit var apiKey: String
}