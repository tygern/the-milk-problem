package io.milk.database

import com.zaxxer.hikari.HikariDataSource

open class DatabaseSupport {
    fun setupDatabase(): HikariDataSource {
        val dataSource = HikariDataSource()
        dataSource.jdbcUrl = System.getenv("JDBC_DATABASE_URL")
        dataSource.username = System.getenv("JDBC_DATABASE_USERNAME")
        dataSource.password = System.getenv("JDBC_DATABASE_PASSWORD")
        dataSource.maximumPoolSize = 5
        return dataSource
    }
}