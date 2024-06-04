package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.dto.*
import com.example.movie_ticketing.repository.MovieRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import java.io.InputStream
import java.time.LocalDate
import java.util.*


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
    fun searchMovies(query: String,page:Int): MovieSearchResult {
        val url = "https://api.themoviedb.org/3/search/movie?api_key=$apiKey&query=$query&language=ko-KR"
        val result = restTemplate.getForObject(url, MovieSearchResult::class.java) ?: throw Exception("Movie not found")
        return sortMoviesByPopularity(result)
    }


    /**
     * 검색 결과를 인기도 순으로 정렬
     */
    private fun sortMoviesByPopularity(result: MovieSearchResult): MovieSearchResult {
        val sortedMovies = result.movies.sortedByDescending { it.popularity }
        return MovieSearchResult(
            page = result.page,
            movies= sortedMovies,
            total_pages = result.total_pages,
            total_results = result.total_results
        )
    }

    // 개봉일이 2024-05-01 ~ 2024-06-01일 사이이면서 지역이 한국인 영화를 찾아옴.
    // MovieSearchResult 의 반환값이 List<MovieDetails> 이기 때문에 thymeleaf 문법으로 ${movie.posterPath} 할 수 있음
    @Transactional //앞으로도 개봉일 따라서 가져올거기에 db에 저장해야함 (유저용/admin용 나눠서 해야할지도)
                    //인기무비 10개씩만 저장! //현재 날짜 기준으로 한달씩 -> 매일매일 업로드 가능
    fun getBoxOffice(currentDate: LocalDate,page: Int) : MovieSearchResult {
        val monthDate  =currentDate.plusMonths(1)
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=ko-KR&region=KR&release_date.gte=2024-05-01&release_date.lte=${monthDate}&page=$page&include_adult=false&vote_average.gte=1"
        val result = restTemplate.getForObject(url, MovieSearchResult::class.java) ?: throw Exception("API 영화 호출 실패")
        if(page ==1) { //1 페이지 인기순위만 가져옴 다른거 필요 x
            val top10movie = result.movies.sortedByDescending { it.popularity }.take(10)

            top10movie.forEach { movieDetails ->
                savemovie(movieDetails)
            }
        }
        return sortMoviesByPopularity(result)
    }



    fun getMoviesFromJson(): List<MovieDetails> {
        val jsonInputStream: InputStream = ClassPathResource("movies.json").inputStream
        val movieResponse: MovieResponse = objectMapper.readValue(jsonInputStream)
        return movieResponse.results
    }

    fun getCast(movieId: Int): CastName {
       val credits = findCreditList(movieId)
        val castList = mutableListOf<String>()
        // 디렉터 뽑기 first로 오는 디렉터만 저장후 이후는 다 저장x
        credits.crew.firstOrNull { crew -> crew.knownForDepartment == "Directing" }?.let {
            castList.add(it.name) //crew리스트에서 각항목의 name필드값을 castList에 추가
        }

        // Acting인 cast만 저장하기
        credits.cast.filter { cast -> cast.knownForDepartment == "Acting" }
            .take(5)
            .forEach { cast ->
                castList.add(cast.name)
            }

        val creditList =CastName(
             name = castList
        )

        return creditList  // 이제 정상적으로 take 사용 가능
    }
    // movie 완전 저장
// 즉, tmdbid를 기반으로 없는 영화일 경우 저장하면서 출연진과 역할들도!
        @Transactional//여러번 데이터베이스 연산시킴 (forEach 도중에 오류나면 다시 되돌아가기 위함)
    fun savemovie(movieDetails: MovieDetails){
        val tmdbid = movieDetails.id!! //영화 제목은 무조건 있을테니까
        if(movieRepository.findById(tmdbid).isEmpty) {
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
                .take(5) //최대 10명 취하고
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
    //무비 상태 업데이트 -> 인기수 10개 state=1
    //이거는 매달 1일마다 같이 사용하면 될듯
    @Transactional //여러번 데이터베이스 연산떄문에 도중에 오류생기면 이상하게 바뀔 여지가 있기에 사용
    fun updateMovieStates(){
        val movies =movieRepository.findAll().filterNotNull()
    //filterNotNull() => null값을 제거하고 List<Movie>를 반환 ->널이 있는 리스트를 제거
    //그냥 널아님 !! 연산자 해도 되는데 혹시모르니까 필터로 거치고 decending
    //원래 findall 하면 List<Movie?> 이런식으로 나옴
        val updateMovies = movies.sortedByDescending { it.popularity }.take(10)
        movies.forEach{it.state =false}    //전체적인 영화들 다시 state조정
        updateMovies.forEach{it.state = true} //업데이트 하는 무비만 state=1

          movieRepository.saveAll(movies)
    }





    //크레딧들을 리스트형태로 모두 가져옴
    fun findCreditList(movieId: Int): CreditList {
        val url = "https://api.themoviedb.org/3/movie/$movieId/credits?api_key=$apiKey&language=ko-KR"
        return restTemplate.getForObject(url, CreditList::class.java) ?: throw Exception("API 배우 호출 실패")
    }



    //moviedetails를 가져와서 이거를 무비타입으로 맵핑
    fun convertmovie(movieDetails: MovieDetails):Movie{
        val movie = Movie() //변환을하는데  무조건 ""이나 null값이 아니어야함 즉
        // ?타입을 널이 안들어가는 것을 확신시켜야해
        //movie.tmdbid = movieDetails.id!!  영화id는 필수
        movie.id =movieDetails.id
        movie.title = movieDetails.title!!
        movie.popularity = movieDetails.popularity ?: 0.0
        movie.openDate = movieDetails.openDate ?: LocalDate.now()
        movie.cast=""
        movie.role=""

        return movie
    }
    fun getBoxOfficeForMovie(currentDate: LocalDate, page: Int): List<MovieDetails> {
        val monthDate = currentDate.plusMonths(1)
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=ko-KR&region=KR&vote_average.gte=1&release_date.gte=2024-05-01&release_date.lte=${monthDate}&page=$page"
        val result = restTemplate.getForObject(url, MovieSearchResult::class.java) ?: throw Exception("API 영화 호출 실패")
        val top10Movies = result.movies.sortedByDescending { it.popularity }.take(10)
        return top10Movies.map { movieSummary -> retrieveMovieDetails(movieSummary.id) }
    }


}
