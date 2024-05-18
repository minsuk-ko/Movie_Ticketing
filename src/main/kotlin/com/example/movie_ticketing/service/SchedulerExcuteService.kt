package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Schedule
import com.example.movie_ticketing.repository.MovieRepository
import com.example.movie_ticketing.repository.ScheduleRepository
import com.example.movie_ticketing.repository.TheaterRepository
import org.springframework.web.bind.annotation.RequestParam
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

//만든이유 하루마다 매일매일 스케줄 업데이트하려고
class SchedulerExcuteService (private val scheduleRepository: ScheduleRepository,
                              private val movieRepository: MovieRepository,
                              private val theaterRepository: TheaterRepository,
                              private val movieService: MovieService,
                             private val scheduleService: ScheduleService
) {
    private val scheduler = Executors.newScheduledThreadPool(1)

    fun startDailyTask() {
        val initialDelay = calculateInitialDelay()
        val period = TimeUnit.DAYS.toMillis(1) // 24시간을 밀리초로 계산

        scheduler.scheduleAtFixedRate({
            createScheduleForDay(LocalDate.now().plusDays(30)) // 현재 날짜로부터 30일 후의 스케줄을 생성
        }, initialDelay, period, TimeUnit.MILLISECONDS)  //매일마다 이 스케줄을 실행한다!
    }

    private fun calculateInitialDelay(): Long {
        val now = LocalTime.now()
        val nextRun = LocalTime.MIDNIGHT // 자정에 실행
        val delay = Duration.between(now, nextRun).toMillis()
        return if (delay > 0) delay else TimeUnit.DAYS.toMillis(1) + delay // 이미 자정이 지났다면 다음 날 자정까지 기다림
    }

    fun createScheduleForDay(scheduleDate: LocalDate) {
     //   movieService.getBoxOffice(scheduleDate, ) //현재날짜 +30일 까지의 영화들 가져와서 확인
        val currentDate = LocalDate.now() //현재날짜꺼내기
        val scheduleDate = currentDate.plusDays(30) //현재 날짜 + 30
        val theaters = theaterRepository.findAll()
        val top10 = movieRepository.findTop10ByStateTrueAndOpenDateBeforeOrderByPopularityDesc(scheduleDate)
        //select * from movie where state = true and open_date<'date' oreder by popularity desc limit 10;
        // 즉 무비리스트 10개 제한걸고 스테이트랑 오픈데이트 비교해서 가져오는 거
        // state가 1인거만 가져오면 open_date가 현재날짜 이후인 경우가 있을 수 있음!

        movieService.updateMovieStates()// 인기순위 10개 state=1


            for (theater in theaters) {
                //1~7관까지 각각의 상영관에서 영화들을 랜덤으로 선택한다
                //인기순위 1등은 1,2관 차지 나머지는 다 랜덤으로 배정
                val selectedMovies = if (theater.id in 1..2) { //1관 2관
                    top10.firstOrNull()?.let { listOf(it) } ?: continue //?.let 을통해 null이 아닌 경우에만 함수를 호출하고 listOf(it)을 통해 리스트를 생성
                } else {//나머지 영화 중에서 랜덤으로 3~4개 선택
                    if (top10.size > 1) top10.drop(1).shuffled().take((3..4).random())
                    // 리스트 크기가 1보다 클 경우 첫 번째 영화 삭제후 2~3개를 랜덤으로 선택
                    else continue
                    //영화가 1개이하라면 다음 반복으로
                }


                var startTime = LocalTime.of((8..10).random(), 0)
                //8,9,10 시 시작시간 랜덤 설정
                while (startTime.plusHours(2) < LocalTime.of(22, 0)) //22시 전까지 계속반복
                { val movie =selectedMovies.shuffled().find {
                    scheduleService.movieCompareDate(it.id, scheduleDate)//무비랑 현재 날짜비교
                }
                    if (movie != null)
                    {
                        val schedule = Schedule().apply {
                            this.start = startTime.toString()
                            this.end = startTime.plusHours(2).toString()//일단 2시간으로고정
                            this.date = scheduleDate.toString()
                            this.movie = movie
                            this.theater = theater
                        }
                        scheduleRepository.save(schedule)
                        startTime = startTime.plusHours(3) //영화 3시간 간격
                        if (startTime.plusHours(2) >= LocalTime.of(22, 0))
                            break


                    }

                }
            }

        }
    }
