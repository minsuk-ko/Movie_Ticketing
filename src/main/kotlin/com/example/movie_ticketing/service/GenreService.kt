package com.example.movie_ticketing.service

import com.example.movie_ticketing.dto.Genre
import com.example.movie_ticketing.dto.GenreResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class GenreService(private val objectMapper: ObjectMapper) {
    private val genres: Map<Int, String>

    init {
        val jsonFile = ClassPathResource("genres.json").inputStream
        val genreResponse: GenreResponse = jacksonObjectMapper().readValue(jsonFile)
        genres = genreResponse.genres.associateBy({ it.id }, { it.name })
    }

    fun getGenreName(id: Int): String? {
        return genres[id]
    }

    fun getGenreNames(ids: List<Int>): List<String> {
        return ids.mapNotNull { getGenreName(it) }
    }

    fun getGenresFromJson(): List<Genre> {
        val jsonInputStream: InputStream = ClassPathResource("genres.json").inputStream
        val genreResponse: GenreResponse = objectMapper.readValue(jsonInputStream)
        return genreResponse.genres
    }
}