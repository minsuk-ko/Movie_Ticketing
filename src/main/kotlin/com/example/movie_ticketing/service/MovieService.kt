package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.dto.CreditList
import com.example.movie_ticketing.dto.MovieDetails
import com.example.movie_ticketing.dto.MovieResponse
import com.example.movie_ticketing.dto.MovieSearchResult
import com.example.movie_ticketing.repository.MovieRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.core.io.ClassPathResource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import java.io.InputStream


@Service
class MovieService(private val restTemplate: RestTemplate,
                   private val webClient: WebClient,
                   val movieRepository: MovieRepository,
                   private val objectMapper: ObjectMapper
) {
    @Value("\${tmdb.api.key}")
    private lateinit var apiKey: String

    fun retrieveMovieDetails(movieId: Int): MovieDetails {
        // URL 뒤에 language=ko-KR 을 통해 한글로 가져온다.
        val url = "https://api.themoviedb.org/3/movie/$movieId?api_key=$apiKey&language=ko-KR"
        return restTemplate.getForObject(url, MovieDetails::class.java) ?: throw Exception("Movie not found")
    }

    /**
     * restTemplate.getForObject : URL로부터 객체를 가져오는 데 사용
     * MovieSearchResult::class.java : restTemplate.getForObject 메소드가 TMDB API로부터 반환된 JSON 응답을
     * MovieSearchResult 타입의 객체로 변환하도록 함.
     */
    fun searchMovies(query: String): MovieSearchResult {
        val url = "https://api.themoviedb.org/3/search/movie?api_key=$apiKey&query=$query&language=ko-KR"
        val result = restTemplate.getForObject(url, MovieSearchResult::class.java) ?: throw Exception("Movie not found")
        return sortMoviesByPopularity(result)
    }


    /**
     * 검색 결과를 인기도 순으로 정렬
     */
    fun sortMoviesByPopularity(result: MovieSearchResult): MovieSearchResult {
        // movies 리스트를 인기도순으로 내림차순으로 정렬
        // 인기도가 null 이라면 가장 낮은 값으로 설정
        val sortedMovies = result.movies.sortedByDescending { it.popularity ?: Double.MIN_VALUE }
        return MovieSearchResult(sortedMovies)
    }

    // 개봉일이 2024-05-01 ~ 2024-06-01일 사이이면서 지역이 한국인 영화를 찾아옴.
    // MovieSearchResult 의 반환값이 List<MovieDetails> 이기 때문에 thymeleaf 문법으로 ${movie.posterPath} 할 수 있음
    fun getBoxOffice() : MovieSearchResult {
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=ko-KR&region=KR&release_date.gte=2024-05-01&release_date.lte=2024-06-01"
        val result = restTemplate.getForObject(url, MovieSearchResult::class.java) ?: throw Exception("API 영화 호출 실패")
        result.movies.forEach {
            movieDetails->
                    savemovie(movieDetails)
        }

        return sortMoviesByPopularity(result)
    }

    fun getMoviesFromJson(): List<MovieDetails> {
        val jsonInputStream: InputStream = ClassPathResource("movies.json").inputStream
        val movieResponse: MovieResponse = objectMapper.readValue(jsonInputStream)
        return movieResponse.results
    }
    fun getTopTwoActorsForMovie(movieId: Int): List<String> {
        val url = "https://api.themoviedb.org/3/movie/$movieId/credits?api_key=$apiKey&language=ko-KR"
        val response = restTemplate.getForObject(url, String::class.java) ?: throw Exception("Actors not found")
        val jsonResponse = JSONObject(response)
        val castArray = jsonResponse.getJSONArray("cast")

        val actorsList = mutableListOf<String>()
        for (i in 0 until castArray.length()) {
            val actor = castArray.getJSONObject(i)
            actorsList.add(actor.getString("name"))
        }

        return actorsList.take(2)  // 이제 정상적으로 take 사용 가능
    }

    // movie db저장 및 배우/디렉터
    fun savemovie(movieDetails: MovieDetails){
        val title = movieDetails.title!! //영화 제목은 무조건 있을테니까
        if(movieRepository.findByTitle(title).isEmpty) {
            //제목이 없을경우 리스트들 저장 실행
            val movie = convertmovie(movieDetails)
            println(movieDetails.id)
            val credits=findCreditList(movieDetails.id)
            val castlist = mutableListOf<String>()
            val roleslist = mutableListOf<String>()
            // directing 부서에서 한 명만 선택
            credits.crew.firstOrNull { crew -> crew.knownForDepartment == "Directing" }?.let {
                castlist.add(it.name)
                roleslist.add(it.knownForDepartment)
            } // 조건에 맞는함수 하나만 반환하고 나머지는 다 무시함 즉Directing 첫번째(대부분 주요감독만 배출)
            // acting 부서에서 최대 10명만 선택
            credits.cast.filter { cast -> cast.knownForDepartment == "Acting" }
                .take(10) //최대 10명 취하고
                .forEach { cast ->  //각각의 cast리스트항목마다 추가
                    castlist.add(cast.name)
                    roleslist.add(cast.knownForDepartment)
                }
            //문자열들을 저장한 리스트들을 문자열로변환 구분자는 ,
            val allString = castlist.joinToString(separator = ",")
            val rolesString = roleslist.joinToString(separator = ",")
            movie.role=rolesString
            movie.cast=allString
            movieRepository.save(movie)
        }
    }
    fun findCreditList(movieId: Int): CreditList {
        val url = "https://api.themoviedb.org/3/movie/$movieId/credits?api_key=$apiKey&language=ko-KR"
        return restTemplate.getForObject(url, CreditList::class.java) ?: throw Exception("API 배우 호출 실패")
    }

//  movieResponse.results.forEach { movieDetails ->
//            val movie = convertmovie(movieDetails)
//            movieRepository.save(movie)
//        }// 상영관되어있는거 저장 이거 처음에 셋팅해서 json파일 불러온것들 db에 저장


    //moviedetails를 가져와서 이거를 무비타입으로 맵핑
    fun convertmovie(movieDetails: MovieDetails):Movie{
        val movie = Movie() //변환을하는데  무조건 ""이나 null값이 아니어야함 즉
        // ?타입을 널이 안들어가는 것을 확신시켜야해
        //movie.tmdbid = movieDetails.id!!  영화id는 필수
        movie.story = movieDetails.overview ?: "NO Description"//movieDetails.overview ?:  //설명없음
        movie.isAdult =movieDetails.adult ?: false //값 없으면 미성년자관람
        movie.openDate = movieDetails.openDate!!
        movie.posterUrl ="https://image.tmdb.org/t/p/w500${movieDetails.posterPath}"
        movie.backdropPath= "https://image.tmdb.org/t/p/w500${movieDetails.backdropPath}"
        movie.title=movieDetails.title!!
        movie.runtime=movieDetails.runtime ?: "No DATA"
        movie.cast=""
        movie.role=""
        return movie
    }






}
