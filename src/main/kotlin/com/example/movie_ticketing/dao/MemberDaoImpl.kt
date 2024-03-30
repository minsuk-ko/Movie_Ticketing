package com.example.movie_ticketing.dao

import com.example.movie_ticketing.DataSourceFactory
import com.example.movie_ticketing.JdbcHelper
import com.example.movie_ticketing.Page
import com.example.movie_ticketing.domain.Member
import java.sql.ResultSet

class MemberDaoImpl : MemberDao {

    companion object {
        private const val FIND_ALL =
            "select id, name, email, age, password from member order by id desc limit ?,?"

        private const val FIND_BY_ID =
            "select id, name, email, age, password from member where id=?"

        private const val FIND_BY_MEMBER_EMAIL =
            "select id, name, email, age, password from member where email=?"

        private const val SAVE =
            "insert member(name, email, age, password) values(?, ?, ?, ?) returning *"

        private const val CHANGE_PASSWORD = "update member set password=? where id=?"

        private const val DELETE_BY_ID = "delete from member where id=?"
    }

    private val jdbcHelper = JdbcHelper(DataSourceFactory.dataSource)

    private fun mapMember(rs: ResultSet): Member =
        Member(id = rs.getInt("Member_id"), name = rs.getString("Member_name"),
            email = rs.getString("Member_email"),
            age = rs.getInt("Member_age"),
            password = rs.getString("Member_password"))

    /**
     * 회원 목록
     */
    override fun list(page: Page): List<Member> {
        return jdbcHelper.list(FIND_ALL, page.offset, page.size) { rs ->
            mapMember(rs)
        }
    }

    override fun getById(id: Int): Member? =
        jdbcHelper.get(FIND_BY_ID, id) { rs -> mapMember(rs) }

    override fun getByMemberEmail(email: String): Member? =
        jdbcHelper.get(FIND_BY_MEMBER_EMAIL, email) { rs -> mapMember(rs) }

    override fun join(member: Member): Member? =
        jdbcHelper.get(SAVE, member.name, member.email,
            member.age, member.password) { rs -> mapMember(rs) }

    override fun changePassword(id: Int, password: String) =
        jdbcHelper.update(CHANGE_PASSWORD, password, id)

    override fun deleteById(id: Int) = jdbcHelper.update(DELETE_BY_ID, id)
}