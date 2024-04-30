package com.example.movie_ticketing.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class TmdbService(private val tmdbWebClient: WebClient,@Value("\${tmdb.api.key}") private val apiKey: String) {

    fun fetchMovieDetails(movieId: String): Mono<String> {
        return tmdbWebClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/movie/$movieId")
                    .queryParam("api_key", apiKey)
                    .build()
            }
            .retrieve()
            .bodyToMono(String::class.java)
    }
}


