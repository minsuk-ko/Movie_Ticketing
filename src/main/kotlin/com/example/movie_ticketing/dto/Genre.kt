package com.example.movie_ticketing.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class Genre (
    @JsonProperty("id") val id: Int,
@JsonProperty("name") val name: String
    )
data class GenreResponse(
    val genres: List<Genre>
)