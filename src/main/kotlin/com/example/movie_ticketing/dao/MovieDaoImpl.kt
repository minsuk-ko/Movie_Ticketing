package com.example.movie_ticketing.dao

import com.example.movie_ticketing.domain.Movie
import com.example.movie_ticketing.map
import org.springframework.data.domain.Page
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class MovieDaoImpl(private val template: NamedParameterJdbcTemplate) : MovieDao {

    companion object{
        private const val FIND_BY_ID =
            "select id, title, story, director, actor, posterURL, openDate, runtime, rating, state from movie where id=?"
        private const val SAVE =
            "insert member(title, story, director, actor, posterURL, openDate, runtime, rating, state) values(?, ?, ?, ?, ?, ?, ?, ?, ?) returning *"
        private const val DELETE_BY_ID = "delete from movie where id=?"
        private const val FIND_BY_TITLE_CONTAINING = "select p from Movie as p where p.title =?1"
    }

    private val movieRowMapper = BeanPropertyRowMapper(Movie::class.java)

    override fun findById(id: Int): Movie? =
        template.queryForObject(MovieDaoImpl.FIND_BY_ID, mapOf("id" to id), movieRowMapper)

    override fun save(movie: Movie): Movie? =
        template.queryForObject(MovieDaoImpl.SAVE, movie.map, movieRowMapper)

    override fun deleteById(id: Int) =
        template.update(MovieDaoImpl.DELETE_BY_ID, mapOf("id" to id))

    override fun findByTitleContaining(searchKeyword: String): Page<Movie> {
        TODO("PASS")
    }
}