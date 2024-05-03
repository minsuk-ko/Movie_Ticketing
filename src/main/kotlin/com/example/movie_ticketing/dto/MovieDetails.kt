package com.example.movie_ticketing.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date


data class Genre(
    @JsonProperty("id") val id: Int,
    @JsonProperty("name") val name: String
)
data class MovieDetails(
    @JsonProperty("original_title") val id: String,
    @JsonProperty("poster_path") val posterPath: String?,
    @JsonProperty("genres") val genres: List<Genre>,
    @JsonProperty("overview") val overview: String,
    @JsonProperty("vote_average") val rating: Float,
    @JsonProperty("release_date") val releaseDate: Date
)