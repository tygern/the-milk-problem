package io.milk.start

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import io.milk.database.DatabaseSupport
import io.milk.database.DatabaseTemplate

import io.milk.products.ProductDataGateway
import io.milk.products.ProductService
import org.slf4j.LoggerFactory
import java.util.*

fun Application.module() {
    val logger = LoggerFactory.getLogger(this.javaClass)

    val dataSource = DatabaseSupport().setupDatabase()
    val template = DatabaseTemplate(dataSource)
    val productService = ProductService(ProductDataGateway(template))

    install(DefaultHeaders)
    install(CallLogging)
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    install(ContentNegotiation) {
        jackson()
    }
    install(Routing) {
        get("/") {
            val products = productService.findAll()
            call.respond(FreeMarkerContent("index.ftl", mapOf("products" to products)))
        }
        post("/api/products") {
            val purchase = call.receive<PurchaseInfo>()
            logger.info("received purchase {} {}", purchase.name, purchase.amount)

            val product = productService.findBy(purchase.id)
            logger.info("found product {} {}", product.name, product.quantity)

            product.decrementBy(purchase.amount)

            val updated = productService.update(product)
            logger.info("updated product {} {}", updated.name, updated.quantity)

            call.respond(updated)
        }
        static("images") { resources("images") }
        static("style") { resources("style") }
    }
}

fun main() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Jetty, port, watchPaths = listOf("basic-server"), module = Application::module).start()
}