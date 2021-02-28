package test.milk.products

import io.milk.database.setupDatabase
import io.milk.products.ProductDataGateway
import io.milk.products.ProductTable
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductDataGatewayTest {
    @Before
    fun before() {
        setupDatabase()

        transaction {
            ProductTable.deleteAll()
            stockInventory()
        }
    }

    @Test
    fun create() {
        transaction {
            val gateway = ProductDataGateway()
            val product = gateway.create("eggs", 10)
            assertTrue(product.id > 0)
            assertEquals("eggs", product.name)
            assertEquals(10, product.quantity)
        }
    }

    @Test
    fun selectAll() {
        val gateway = ProductDataGateway()
        val products = gateway.findAll()
        assertEquals(3, products.size)
    }

    @Test
    fun findBy() {
        val gateway = ProductDataGateway()
        val product = gateway.findBy(101)
        assertEquals("milk", product.name)
        assertEquals(42, product.quantity)
    }

    @Test
    fun update() {
        val gateway = ProductDataGateway()
        val product = gateway.findBy(101)

        product.quantity = 44
        gateway.update(product)

        val updated = gateway.findBy(101)
        assertEquals("milk", updated.name)
        assertEquals(44, updated.quantity)
    }

    ///

    private fun stockInventory() {
        transaction {
            ProductTable.insert {
                it[id] = 101
                it[name] = "milk"
                it[quantity] = 42
            } get ProductTable.id

            ProductTable.insert {
                it[id] = 102
                it[name] = "bacon"
                it[quantity] = 52
            } get ProductTable.id

            ProductTable.insert {
                it[id] = 103
                it[name] = "tuna"
                it[quantity] = 62
            } get ProductTable.id
        }
    }
}