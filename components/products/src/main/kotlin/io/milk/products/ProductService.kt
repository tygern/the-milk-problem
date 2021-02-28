package io.milk.products

class ProductService(val dataGateway: ProductDataGateway) {
    fun findAll(): List<ProductInfo> {
        return dataGateway.findAll().map { ProductInfo(it.id, it.name, it.quantity) }
    }

    fun findBy(id: Long): ProductInfo {
        val record = dataGateway.findBy(id)
        return ProductInfo(record.id, record.name, record.quantity)
    }

    fun incrementBy(product: ProductInfo, quantity: Int) {
        val record = dataGateway.findBy(product.id)
        record.quantity += quantity
        dataGateway.update(record)
    }

    fun decrementBy(product: ProductInfo, quantity: Int) {
        val record = dataGateway.findBy(product.id)
        record.quantity -= quantity
        dataGateway.update(record)
    }
}