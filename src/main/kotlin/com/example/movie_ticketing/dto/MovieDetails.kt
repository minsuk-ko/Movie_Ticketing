package com.example.movie_ticketing.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class Genre(
    @JsonProperty("id") val id: Int,
    @JsonProperty("name") val name: String
)

data class MovieDetails(
    @JsonProperty("id") val id: Int,
    @JsonProperty("adult") val adult: Boolean?,
    //@JsonProperty("original_language") val originalLanguage: String?,
    //@JsonProperty("original_title") val originalTitle: String?,
    @JsonProperty("title") val title: String?,
    @JsonProperty("poster_path") val posterPath: String?,
    @JsonProperty("backdrop_path") val backdropPath: String?,
    @JsonProperty("genres_id") val genres: List<Genre>?,
    @JsonProperty("overview") val overview: String?,
    @JsonProperty("vote_average") val rating: Float?,
    //@JsonProperty("popularity") val popularity: Float?,
    @JsonProperty("release_date") val openDate: Date?
)

data class MovieSearchResult(
    @JsonProperty("results") val movies: List<MovieDetails>
)
data class MovieResponse(
    val results: List<MovieDetails>
)