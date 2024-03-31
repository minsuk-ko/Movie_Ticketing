//package com.example.movie_ticketing.dao
//
//import com.example.movie_ticketing.Page
//import com.example.movie_ticketing.domain.Member
//import com.example.movie_ticketing.map
//import org.springframework.jdbc.core.BeanPropertyRowMapper
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
//import org.springframework.stereotype.Repository
//
//@Repository
//class MemberDaoImpl(private val template: NamedParameterJdbcTemplate) : MemberDao {
//
//    companion object {
//        private const val FIND_ALL =
//            "select id, name, email, age, password from member order by id desc limit ?,?"
//
//        private const val FIND_BY_ID =
//            "select id, name, email, age, password from member where id=?"
//
//        private const val FIND_BY_EMAIL =
//            "select id, name, email, age, password from member where email=?"
//
//        private const val SAVE =
//            "insert member(name, email, age, password) values(?, ?, ?, ?) returning *"
//
//        private const val CHANGE_PASSWORD = "update member set password=? where id=?"
//
//        private const val DELETE_BY_ID = "delete from member where id=?"
//    }
//
//
//    private val memberRowMapper = BeanPropertyRowMapper(Member::class.java)
//
//    override fun list(page: Page): List<Member> =
//        template.query(FIND_ALL, page.map, memberRowMapper)
//
//    override fun findById(id: Int): Member? =
//        template.queryForObject(FIND_BY_ID, mapOf("id" to id), memberRowMapper)
//
//    override fun findByEmail(email: String): Member? =
//        template.queryForObject(FIND_BY_EMAIL, mapOf("email" to email),
//            memberRowMapper)
//
//    override fun save(member: Member): Member? =
//        template.queryForObject(SAVE, member.map, memberRowMapper)
//
//    override fun changePassword(id: Int, password: String) =
//        template.update(CHANGE_PASSWORD, mapOf("id" to id, "password" to password))
//
//    override fun deleteById(id: Int) =
//        template.update(DELETE_BY_ID, mapOf("id" to id))
//}