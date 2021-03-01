package io.milk.products

import io.milk.database.DatabaseTemplate

class ProductDataGateway(private val template: DatabaseTemplate) {

    fun create(name: String, quantity: Int): ProductRecord {
        return template.create(
            "insert into products (name, quantity) values (?, ?)", { id ->
                ProductRecord(id, name, quantity)
            }, name, quantity
        )
    }

    fun findAll(): List<ProductRecord> {
        return template.findAll("select id, name, quantity from products") { rs ->
            ProductRecord(rs.getLong(1), rs.getString(2), rs.getInt(3))
        }
    }

    fun findBy(id: Long): ProductRecord? {
        return template.findBy(
            "select id, name, quantity from products where id = ?", { rs ->
                ProductRecord(rs.getLong(1), rs.getString(2), rs.getInt(3))
            }, id
        )
    }

    fun update(product: ProductRecord): ProductRecord {
        template.update(
            "update products set name = ?, quantity = ? where id = ?",
            product.name, product.quantity, product.id
        )
        return product
    }
}