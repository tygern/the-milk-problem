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
import io.milk.database.setupDatabase
import io.milk.products.ProductDataGateway
import io.milk.products.ProductService
import java.util.*

fun Application.module() {
    setupDatabase()
    val productService = ProductService(ProductDataGateway())

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
            val product = productService.findBy(purchase.id)
            product.decrementBy(purchase.amount)
            val updated = productService.update(product)
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