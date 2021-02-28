package test.milk.start

import io.ktor.http.*
import io.ktor.server.testing.*
import io.milk.start.module
import org.junit.Before
import org.junit.Test
import test.milk.TestScenarioSupport
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class AppTest {
    @Before
    fun before() = TestScenarioSupport().loadTestScenario("products")

    @Test
    fun testIndex() = testApp {
        handleRequest(HttpMethod.Get, "/").apply {
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

    private fun testApp(callback: TestApplicationEngine.() -> Unit) {
        withTestApplication({ module() }) { callback() }
    }
}