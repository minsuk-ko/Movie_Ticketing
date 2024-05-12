package com.example.movie_ticketing.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date


data class MovieDetails(
    val id: Int,
    @JsonProperty("adult") val isAdult: Boolean,
    @JsonProperty("backdrop_path") val backdropPath: String?,
    @JsonProperty("genre_ids") val genreIds: List<Int>,
    @JsonProperty("original_language") val originalLanguage: String,
    @JsonProperty("original_title") val originalTitle: String,
    @JsonProperty("overview") val overview: String,
    @JsonProperty("popularity") val popularity: Double,
    @JsonProperty("poster_path") val posterPath: String?,
    @JsonProperty("release_date") val releaseDate: Date,
    val title: String,
    val video: Boolean,
    @JsonProperty("vote_average") val voteAverage: Double,
    @JsonProperty("vote_count") val voteCount: Int
)
data class MovieResponse(
    val page: Int,
    val results: List<MovieDetails>,
    @JsonProperty("total_pages") val totalPages: Int,
    @JsonProperty("total_results") val totalResults: Int
)