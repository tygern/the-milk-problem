package test.milk.products


import io.milk.database.DatabaseSupport
import io.milk.database.DatabaseTemplate
import io.milk.products.ProductDataGateway
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductDataGatewayTest {
    private val dataSource = DatabaseSupport().setupDatabase()
    private val template = DatabaseTemplate(dataSource)

    @Before
    fun before() {
        DatabaseTemplate(dataSource).apply {
            execute("delete from products")
            execute("insert into products(id, name, quantity) values (101, 'milk', 42)")
            execute("insert into products(id, name, quantity) values (102, 'bacon', 52)")
            execute("insert into products(id, name, quantity) values (103, 'tuna', 62)")
        }
    }

    @Test
    fun create() {
        val gateway = ProductDataGateway(template)
        val product = gateway.create("eggs", 10)
        assertTrue(product.id > 0)
        assertEquals("eggs", product.name)
        assertEquals(10, product.quantity)
    }

    @Test
    fun selectAll() {
        val gateway = ProductDataGateway(template)
        val products = gateway.findAll()
        assertEquals(3, products.size)
    }

    @Test
    fun findBy() {
        val gateway = ProductDataGateway(template)
        val product = gateway.findBy(101)!!
        assertEquals("milk", product.name)
        assertEquals(42, product.quantity)
    }

    @Test
    fun update() {
        val gateway = ProductDataGateway(template)
        val product = gateway.findBy(101)!!

        product.quantity = 44
        gateway.update(product)

        val updated = gateway.findBy(101)!!
        assertEquals("milk", updated.name)
        assertEquals(44, updated.quantity)
    }
}