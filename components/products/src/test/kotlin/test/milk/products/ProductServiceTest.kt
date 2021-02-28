package test.milk.products

import io.milk.database.setupDatabase
import io.milk.products.ProductDataGateway
import io.milk.products.ProductService
import io.milk.products.ProductTable
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ProductServiceTest {
    @Before
    fun before() {
        setupDatabase()

        transaction {
            ProductTable.deleteAll()
            stockInventory()
        }
    }

    @Test
    fun findAll() {
        val service = ProductService(ProductDataGateway())
        val products = service.findAll()
        assertEquals(2, products.size)
    }

    @Test
    fun findBy() {
        val service = ProductService(ProductDataGateway())
        val product = service.findBy(101)
        assertEquals("milk", product.name)
        assertEquals(42, product.quantity)
    }

    @Test
    fun update() {
        val service = ProductService(ProductDataGateway())
        val info = service.findBy(101)
        info.quantity += 2
        service.update(info)

        val product = service.findBy(101)
        assertEquals("milk", product.name)
        assertEquals(44, product.quantity)
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
                it[name] = "kombucha"
                it[quantity] = 15
            } get ProductTable.id
        }
    }
}