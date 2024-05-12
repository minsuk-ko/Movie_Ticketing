package com.example.movie_ticketing.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class Genre(
    @JsonProperty("id") val id: Int,
    @JsonProperty("name") val name: String
)
data class MovieDetails(
    @JsonProperty("id") val id: Int,
    @JsonProperty("title") val title: String?,
    @JsonProperty("poster_path") val posterPath: String?,
    @JsonProperty("backdrop_path") val backdropPath: String?,
    @JsonProperty("genres") val genres: List<Genre>?,
    @JsonProperty("overview") val overview: String?,
    @JsonProperty("vote_average") val rating: Float?,
    @JsonProperty("release_date") val openDate: Date?
)

data class MovieSearchResult(
    @JsonProperty("results") val movies: List<MovieDetails>
)