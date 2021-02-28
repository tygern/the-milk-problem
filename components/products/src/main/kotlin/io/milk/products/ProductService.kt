package io.milk.products

class ProductService(val dataGateway: ProductDataGateway) {
    fun findAll(): List<ProductInfo> {
        return dataGateway.findAll().map { ProductInfo(it.id, it.name, it.quantity) }
    }

    fun findBy(id: Long): ProductInfo {
        val record = dataGateway.findBy(id)
        return ProductInfo(record.id, record.name, record.quantity)
    }

    fun update(product: ProductInfo) : ProductInfo {
        val record = dataGateway.findBy(product.id)
        record.quantity = product.quantity
        dataGateway.update(record)
        return findBy(record.id)
    }
}