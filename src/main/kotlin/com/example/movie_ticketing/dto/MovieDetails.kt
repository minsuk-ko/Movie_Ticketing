package com.example.movie_ticketing.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
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
    @JsonProperty("release_date") val openDate: Date?,
    @JsonProperty("popularity") val popularity: Double?
)

data class MovieSearchResult(
    val page: Int,
    val results: List<MovieDetails>,
    val total_pages: Int,
    val total_results: Int
)

data class MovieResponse(
    val page: Int,
    val results: List<MovieDetails>,
    @JsonProperty("total_pages") val totalPages: Int,
    @JsonProperty("total_results") val totalResults: Int
)