package io.milk.sales

import io.milk.workflow.WorkFinder
import org.slf4j.LoggerFactory

class PurchaseGenerator : WorkFinder<PurchaseTask> {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun findRequested(name: String): List<PurchaseTask> {
        logger.info("someone purchased some milk!")

        return mutableListOf(PurchaseTask(101, "milk", (1..4).random()))
    }

    override fun markCompleted(info: PurchaseTask) {
        logger.info("completed purchase")
    }
}