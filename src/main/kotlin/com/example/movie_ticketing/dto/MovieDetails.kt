package com.example.movie_ticketing.dto

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import java.time.LocalDate
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
    @JsonProperty("genres") val genres: List<Genre>?,
    @JsonProperty("overview") val overview: String?,
    @JsonProperty("vote_average") val rating: Float?,
    @JsonProperty("popularity") val popularity: Double?,
    @JsonProperty("release_date") val openDate: LocalDate?,
    @JsonProperty("runtime") val runtime: Int?,
    @JsonProperty("videos") val videos : Videos?,
    @JsonProperty("images") val images : Images?
)
data class MovieSearchResult(
    @JsonProperty("page") val page: Int,
    @JsonProperty("results") val movies: List<MovieDetails>,
    @JsonProperty("total_pages") val totalPages: Int,
    @JsonProperty("total_results") val totalResults: Int
)

data class MovieResponse(
    val page: Int,
    val results: List<MovieDetails>,
    @JsonProperty("total_pages") val totalPages: Int,
    @JsonProperty("total_results") val totalResults: Int
)

data class Videos(
    val results: List<VideoResult>
)

data class VideoResult(
    val iso_639_1: String,
    val iso_3166_1: String,
    val name: String,
    val key: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    val published_at: String,
    val id: String
)

data class Images(
    @JsonProperty("backdrops") val backdrops: List<ImageCategory>,
    @JsonProperty("logos") val logos: List<ImageCategory>,
    @JsonProperty("posters") val posters: List<ImageCategory>
)

data class ImageCategory(
    val aspect_ratio: Double,
    val height: Int,
    val iso_639_1: String?,
    @JsonProperty("file_path") val filePath: String?,
    val vote_average: Double,
    val vote_count: Int,
    val width: Int
)