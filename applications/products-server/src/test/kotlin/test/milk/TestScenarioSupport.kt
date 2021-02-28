package test.milk

import io.milk.database.setupDatabase
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

class TestScenarioSupport {
    fun loadTestScenario(name: String) {
        setupDatabase()
        this.javaClass.classLoader.getResourceAsStream("scenarios/" + name + ".sql").reader().readLines()
            .asSequence()
            .filterNot(String::isNullOrBlank)
            .forEach {
                transaction {
                    TransactionManager.current().exec(it)
                }
            }
    }
}