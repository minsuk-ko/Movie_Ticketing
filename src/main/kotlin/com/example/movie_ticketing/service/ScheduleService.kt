package com.example.movie_ticketing.service

import com.example.movie_ticketing.domain.Schedule
import com.example.movie_ticketing.domain.Theater
import com.example.movie_ticketing.repository.MovieRepository
import com.example.movie_ticketing.repository.ScheduleRepository
import com.example.movie_ticketing.repository.TheaterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime

@Service
class ScheduleService(private val movieRepository: MovieRepository,
                      private val scheduleRepository:ScheduleRepository,
                      private val theaterRepository: TheaterRepository,
                      private val movieService: MovieService
    ){



    @Transactional //상영관 자동 생성 만약 있을 경우엔 실행 안함
    fun initializeTheaters() {
        val theaterNames = listOf("1관", "2관", "3관", "4관", "5관", "6관", "7관")

        theaterNames.forEach { name ->
            if (!theaterRepository.existsByName(name)) {
                //select 1 from theater where name = ?  만약에 있으면 숫자1을 반환한다는 뜻
                //select 1은 단순히 숫자 1을 반환하는 용도 필드의 데이터값이 아닌 true/false값만 가져오기 위해서 사용
                val theater = Theater().apply { this.name = name }
                theaterRepository.save(theater)
            }
        }
    }




    fun createSchedule(){
        val currentDate = LocalDate.now() //현재날짜꺼내기
        val theaters = theaterRepository.findAll() //theaters 리스트 반환 => 1에서 7관 있겠죠~ 인정? ㅇㅇㅈ
        for(i in 0 until 30 )// 0<= i <30  //30일 스케줄 만드는거야
        {
            val scheduleDate = currentDate.plusDays(i.toLong()) //현재 날짜 + i
            movieService.updateMovieStates()// 인기순위 10개 state=1
            val top10 = movieRepository.findTop10ByStateTrueAndOpenDateBeforeOrderByPopularityDesc(scheduleDate)
            //select * from movie where state = true and open_date<'date' oreder by popularity desc limit 10;
            // 즉 무비리스트 10개 제한걸고 스테이트랑 오픈데이트 비교해서 가져오는 거
            // state가 1인거만 가져오면 open_date가 현재날짜 이후인 경우가 있을 수 있음!
            for (theater in theaters) {
                val selectedMovies = if (theater.id in 1..2) { //1관 2관
                    top10.firstOrNull()?.let { listOf(it) } ?: continue
                } else {//나머지 영화 중에서 랜덤으로 3~4개 선택
                    if (top10.size > 1) top10.drop(1).shuffled().take((3..4).random())
                    // 리스트 크기가 1보다 클 경우 첫 번째 영화 삭제후 2~3개를 랜덤으로 선택
                    else continue
                    //영화가 1개이하라면 다음 반복으로
                }


                var startTime = LocalTime.of((8..10).random(), 0)
                //8,9,10 시 시작시간 랜덤 설정
                while (true) //22시 전까지 계속반복
                {    if (startTime<LocalTime.of(8,0)||startTime.plusHours(2) >= LocalTime.of(22, 0)) {
                    break  //08~22시 사이 아니면 빠져나오기
                }
                    val movie =selectedMovies.shuffled().find {
                    movieCompareDate(it.id, scheduleDate)//무비랑 현재 날짜비교
                    }
                    if (movie != null )
                        {
                            val schedule = Schedule().apply {
                                this.start = startTime
                                this.end = startTime.plusHours(2)//일단 2시간으로고정
                                this.date = scheduleDate
                                this.movie = movie
                                this.theater = theater
                            }
                            scheduleRepository.save(schedule)
                            startTime = startTime.plusHours(3) //영화 3시간 간격
                        }
                       else {
                        break
                    }
                }
                }
            }

        }


    //무비 openDate랑 예약하는 날짜랑 비교하는거
// 상영스케줄러랑 비교해서 인기순위10위지만 예매날짜 안 맞을경우에 실행 못하도록
    fun movieCompareDate(movieId: Int,reservationDate:LocalDate):Boolean{
        // findById가 Optional<Movie?> 이어서 Optional떼내고
        val optionalmovie= movieRepository.findById(movieId)

        val movie = optionalmovie.orElseThrow{Exception("Movie not found")} //optinal검증
        return movie!!.openDate.isBefore(reservationDate) //movie를 찾았으면 무조건 null이아님
        //오픈데이트가 현재 날짜보다 이전일경우 트루를 반환 if문안에 넣으면 될듯
    }




}
