package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Schedule
import com.example.movie_ticketing.dto.MovieSearchResult
import com.example.movie_ticketing.repository.MovieRepository
import com.example.movie_ticketing.repository.ScheduleRepository
import com.example.movie_ticketing.repository.TheaterRepository
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Service
//만든이유 하루마다 매일매일 스케줄 업데이트하려고
// 서비스 어노테이션을 읽으면서 자동적으로 startdailyTask도 동작하게끔 @PostConstruct도 설정
// 그러나 애플리케이션이 중지해도 startDailyTask는 돌수도 있기에 detroy 메소드를 이용해 스케줄 생성을 멈추게한다.
class SchedulerExcuteService (private val scheduleRepository: ScheduleRepository,
                              private val movieRepository: MovieRepository,
                              private val theaterRepository: TheaterRepository,
                              private val movieService: MovieService,
                             private val scheduleService: ScheduleService,
                              private val restTemplate: RestTemplate
) {
    @Value("\${tmdb.api.key}")
    private lateinit var apiKey: String

    private val scheduler = Executors.newScheduledThreadPool(1)

    @PostConstruct//
    fun startDailyTask() {
        scheduler.scheduleAtFixedRate({
            println("스케줄생성")
            createScheduleForDay(LocalDate.now().plusDays(30)) // 현재 날짜로부터 30일 후의 스케줄을 생성
        }, 0, 1, TimeUnit.DAYS)  // 0= 메소드 시작 즉시 period = 딜레이

    }


    @PreDestroy
    fun detroy() {
        scheduler.shutdownNow()
    }


    fun createScheduleForDay(scheduleDate: LocalDate) {
        //만약 30일 이후의 스케줄이 이미 생성되어있고 다시 실행한 상황이라면 스케줄 생성x
        val schedules = scheduleRepository.findByDate(scheduleDate)
        if (schedules.isEmpty()) {
            val theaters = theaterRepository.findAll()

            val url =
                "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&language=ko-KR&region=KR&include_adult=false&release_date.gte=2024-05-08&release_date.lte=${scheduleDate}&page=1"
            val result =
                restTemplate.getForObject(url, MovieSearchResult::class.java) ?: throw Exception("API 영화 호출 실패")

            val top10movie = result.movies.sortedByDescending { it.popularity }.take(10)

            top10movie.forEach { movieDetails ->
                movieService.savemovie(movieDetails) // 스케줄을 만드는거기에 인기영화를 자주
            }

            movieService.updateMovieStates()// 인기순위 10개 state=1
            val top10 = movieRepository.findTop10ByStateTrueAndOpenDateBeforeOrderByPopularityDesc(scheduleDate)
            //select * from movie where state = true and open_date<'date' oreder by popularity desc limit 10;
            // 즉 무비리스트 10개 제한걸고 스테이트랑 오픈데이트 비교해서 가져오는 거
            // state가 1인거만 가져오면 open_date가 현재날짜 이후인 경우가 있을 수 있음!


            for (theater in theaters) {
                //1~7관까지 각각의 상영관에서 영화들을 랜덤으로 선택한다
                //인기순위 1등은 1,2관 차지 나머지는 다 랜덤으로 배정
                val selectedMovies = if (theater.id in 1..2) { //1관 2관
                    top10.firstOrNull()?.let { listOf(it) }
                        ?: continue //?.let 을통해 null이 아닌 경우에만 함수를 호출하고 listOf(it)을 통해 리스트를 생성
                } else {//나머지 영화 중에서 랜덤으로 3~4개 선택
                    if (top10.size > 1) top10.drop(1).shuffled().take((3..4).random())
                    // 리스트 크기가 1보다 클 경우 첫 번째 영화 삭제후 2~3개를 랜덤으로 선택
                    else continue
                    //영화가 1개이하라면 다음 반복으로
                }

                var startTime = LocalTime.of((8..10).random(), 0)
                //8,9,10 시 시작시간 랜덤 설정
                while (true) //22시 전까지 계속반복
                {
                    if (startTime < LocalTime.of(8, 0) || startTime.plusHours(2) >= LocalTime.of(22, 0)) {
                        break  //08~22시 사이 아니면 빠져나오기
                    }
                    val movie = selectedMovies.shuffled().find {
                        scheduleService.movieCompareDate(it.id, scheduleDate)//무비랑 현재 날짜비교
                    }
                    if (movie != null) {
                        val schedule = Schedule().apply {
                            this.start = startTime
                            this.end = startTime.plusHours(2)//일단 2시간으로고정
                            this.date = scheduleDate
                            this.movie = movie
                            this.theater = theater
                        }
                        scheduleRepository.save(schedule)
                        startTime = startTime.plusHours(3) //영화 3시간 간격
                    } else {
                        break
                    }
                }
            }
        }
    }
}


