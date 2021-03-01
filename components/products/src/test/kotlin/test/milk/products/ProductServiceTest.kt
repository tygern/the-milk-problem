package test.milk.products

import io.milk.database.DatabaseSupport
import io.milk.database.DatabaseTemplate
import io.milk.products.ProductDataGateway
import io.milk.products.ProductService
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ProductServiceTest {
    private val dataSource = DatabaseSupport().setupDatabase()
    private val template = DatabaseTemplate(dataSource)

    @Before
    fun before() {
        DatabaseTemplate(dataSource).apply {
            execute("delete from products")
            execute("insert into products(id, name, quantity) values (101, 'milk', 42)")
            execute("insert into products(id, name, quantity) values (102, 'kombucha', 15)")
        }
    }

    @Test
    fun findAll() {
        val service = ProductService(ProductDataGateway(template))
        val products = service.findAll()
        assertEquals(2, products.size)
    }

    @Test
    fun findBy() {
        val service = ProductService(ProductDataGateway(template))
        val product = service.findBy(101)
        assertEquals("milk", product.name)
        assertEquals(42, product.quantity)
    }

    @Test
    fun update() {
        val service = ProductService(ProductDataGateway(template))
        val info = service.findBy(101)
        info.quantity += 2
        service.update(info)

        val product = service.findBy(101)
        assertEquals("milk", product.name)
        assertEquals(44, product.quantity)
    }
}