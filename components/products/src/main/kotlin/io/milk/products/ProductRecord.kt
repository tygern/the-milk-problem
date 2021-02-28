package io.milk.products

data class ProductRecord(val id: Long, val name: String, var quantity: Int) {
    fun incrementBy(count: Int) {
        quantity += count;
    }

    fun decrementBy(count: Int) {
        quantity -= count;
    }
}