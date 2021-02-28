package io.milk.products

import org.jetbrains.exposed.sql.Table

object ProductTable : Table("products") {
    val id = long("id").autoIncrement()
    val name = varchar("name", length = 255)
    val quantity = integer("quantity")
}