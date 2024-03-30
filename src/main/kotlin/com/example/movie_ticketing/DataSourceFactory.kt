package com.example.movie_ticketing

import org.mariadb.jdbc.MariaDbDataSource
import java.util.*
import javax.sql.DataSource

class DataSourceFactory {
    companion object {
        private fun createDataSource(): DataSource {
            val props = Properties()
            props.load(DataSourceFactory::class.java.getResourceAsStream(
                "/application.properties"))
            return MariaDbDataSource(props.getProperty("db.url"))
        }

        val dataSource: DataSource = createDataSource()
    }
}
