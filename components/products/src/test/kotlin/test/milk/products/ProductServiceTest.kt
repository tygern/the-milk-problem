package test.milk.products

import io.milk.database.DatabaseSupport
import io.milk.database.DatabaseTemplate
import io.milk.products.ProductDataGateway
import io.milk.products.ProductService
import io.milk.products.PurchaseInfo
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ProductServiceTest {
    private val dataSource = DatabaseSupport().setupDatabase()
    private val service = ProductService(ProductDataGateway(dataSource))

    @Before
    fun before() {
        DatabaseTemplate(dataSource).apply {
            execute("delete from products")
            execute("insert into products(id, name, quantity) values (101000, 'milk', 42)")
            execute("insert into products(id, name, quantity) values (102000, 'kombucha', 15)")
        }
    }

    @Test
    fun findAll() {
        val products = service.findAll()

        assertEquals(2, products.size)
    }

    @Test
    fun findBy() {
        val product = service.findBy(101000)

        assertEquals("milk", product.name)
        assertEquals(42, product.quantity)
    }

    @Test
    fun update() {
        service.update(PurchaseInfo(101000, "milk", 2))

        val product = service.findBy(101000)
        assertEquals("milk", product.name)
        assertEquals(40, product.quantity)
    }

    @Test
    fun decrementBy() {
        val product = service.decrementBy(PurchaseInfo(101000, "milk", 2))

        assertEquals("milk", product.name)
        assertEquals(40, product.quantity)
    }
}