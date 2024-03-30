package com.example.movie_ticketing

import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource


class JdbcHelper(val ds: DataSource) {

    inline fun <T> list(sql: String, vararg params: Any,
                        mapRow: (ResultSet) -> T): List<T> {
        ds.connection.use { conn ->
            conn.prepareStatement(sql).use { ps ->
                setParameters(ps, params)
                val list = mutableListOf<T>()
                val rs = ps.executeQuery()
                while (rs.next()) list.add(mapRow(rs))
                return list
            }
        }
    }

    inline fun <T> get(sql: String, vararg params: Any,
                       mapRow: (ResultSet) -> T): T? {
        ds.connection.use { conn ->
            conn.prepareStatement(sql).use { ps ->
                setParameters(ps, params)
                val rs = ps.executeQuery()
                return if (rs.next()) mapRow(rs) else null
            }
        }
    }

    fun update(sql: String, vararg params: Any): Int {
        ds.connection.use { conn ->
            conn.prepareStatement(sql).use { ps ->
                setParameters(ps, params)
                return ps.executeUpdate()
            }
        }
    }

    /**
     * preparedStatement에 파라미터를 설정
     */
    fun setParameters(ps: PreparedStatement, params: Array<out Any>) =
        params.forEachIndexed { i, param -> ps.setObject(i + 1, param) }
}