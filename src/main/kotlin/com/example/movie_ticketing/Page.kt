package com.example.movie_ticketing

/**
 * 0 페이지부터 시작
 */
data class Page(val number: Int = 0, val size: Int = 10) {
    /**
     * 목록 시작 위치
     */
    val offset = number * size
}
