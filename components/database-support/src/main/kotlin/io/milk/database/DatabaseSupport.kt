package io.milk.database

import org.jetbrains.exposed.sql.Database
import org.postgresql.Driver

fun setupDatabase() {
    Database.connect(System.getenv("JDBC_DATABASE_URL"),
            Driver::class.java.name,
            System.getenv("JDBC_DATABASE_USERNAME"),
            System.getenv("JDBC_DATABASE_PASSWORD"))
}