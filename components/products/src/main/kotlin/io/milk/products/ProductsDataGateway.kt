package io.milk.products

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class ProductsDataGateway {

    fun create(name: String, quantity: Int): ProductRecord {
        return transaction {
            val id = ProductTable.insert {
                it[ProductTable.name] = name
                it[ProductTable.quantity] = quantity
            } get ProductTable.id
            return@transaction ProductRecord(id, name, quantity)
        }
    }

    fun findAll(): List<ProductRecord> {
        val products = mutableListOf<ProductRecord>()
        transaction {
            for (result in ProductTable.selectAll()) {
                products.add(
                    ProductRecord(
                        id = result[ProductTable.id],
                        name = result[ProductTable.name],
                        quantity = result[ProductTable.quantity]
                    )
                )
            }
        }
        return products
    }

    fun findBy(id: Long): ProductRecord {
        return transaction {
            val result = ProductTable.select { ProductTable.id eq id }.single()

            return@transaction ProductRecord(
                id = result[ProductTable.id],
                name = result[ProductTable.name],
                quantity = result[ProductTable.quantity]
            )
        }
    }

    fun update(product: ProductRecord) {
        return transaction {
            ProductTable.update({ ProductTable.id eq product.id }) {
                it[name] = product.name
                it[quantity] = product.quantity
            }
        }
    }
}