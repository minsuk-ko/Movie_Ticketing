package com.example.movie_ticketing.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CastDetails(
    @JsonProperty("id") val id: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("gender") val gender: Int, // 1이 여자, 2가 남자
    @JsonProperty("character") val character: String?, // 배역명, 배우 아니면 없음
    @JsonProperty("profile_path") val profilePath: String?, // 프로필사진
    @JsonProperty("known_for_department")val knownForDepartment: String //역할 - ACTING(배우) or Directing(감독)
)

data class CrewDetails(
    @JsonProperty("id") val id: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("gender") val gender: Int, // 1이 여자, 2가 남자
    @JsonProperty("job") val job: String, // 직업
    @JsonProperty("known_for_department")val knownForDepartment: String, //역할 - ACTING(배우) or Directing(감독)
    @JsonProperty("profile_path") val profilePath: String? // 프로필사진
)

data class CreditList(
    @JsonProperty("cast") val cast: List<CastDetails>,
    @JsonProperty("crew") val crew: List<CrewDetails>
)