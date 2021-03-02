package test.milk.start

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.http.*
import io.ktor.server.testing.*
import io.milk.products.PurchaseInfo
import io.milk.start.module
import io.mockk.clearAllMocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import test.milk.TestScenarioSupport
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
    fun testQuantity() {
        makePurchases("/api/v1/products")

        with(engine) {
            with(handleRequest(io.ktor.http.HttpMethod.Get, "/")) {
                assertTrue(response.content!!.contains("31"))
            }
        }
    }

    ///

    private fun makePurchases(uri: String) {
        runBlocking {
            (1..100).map {
                async {
                    with(engine) {
                        with(handleRequest(HttpMethod.Post, uri) {
                            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                            setBody(mapper.writeValueAsString(PurchaseInfo(42, "milk", 1)))
                        }) {
                            assertEquals(201, response.status()?.value)
                        }
                    }
                }
            }
        }
    }
}