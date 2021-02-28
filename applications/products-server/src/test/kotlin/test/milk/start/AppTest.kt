package test.milk.start

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.http.*
import io.ktor.server.testing.*
import io.milk.products.ProductInfo
import io.milk.start.PurchaseInfo
import io.milk.start.module
import io.mockk.clearAllMocks
import org.junit.Before
import org.junit.Test
import test.milk.TestScenarioSupport
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class AppTest {
    private val engine = TestApplicationEngine()
    private val mapper: ObjectMapper = ObjectMapper().registerKotlinModule()

    @Before
    fun before() {
        clearAllMocks()
        TestScenarioSupport().loadTestScenario("products")
        engine.start(wait = false)
        engine.application.module()
    }

    @Test
    fun testIndex() {
        with(engine) {
            with(handleRequest(HttpMethod.Get, "/")) {
                assertEquals(200, response.status()?.value)
                assertTrue(response.content!!.contains("milk"))
                assertTrue(response.content!!.contains("bacon"))
                assertTrue(response.content!!.contains("tuna"))
                assertTrue(response.content!!.contains("eggs"))
                assertTrue(response.content!!.contains("kombucha"))
                assertTrue(response.content!!.contains("apples"))
                assertTrue(response.content!!.contains("ice tea"))
                assertTrue(response.content!!.contains("yogurt"))
            }
        }
    }

    @Test
    fun testPurchase() {
        with(engine) {
            with(handleRequest(HttpMethod.Post, "/api/products") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(mapper.writeValueAsString(PurchaseInfo(42, "milk", 3)))
            }) {
                assertEquals(200, response.status()?.value)
                val product = mapper.readValue(response.content, ProductInfo::class.java)
                assertEquals(28, product.quantity)
            }
        }
    }
}