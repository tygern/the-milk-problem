package test.milk

import io.milk.database.DatabaseSupport
import io.milk.database.DatabaseTemplate

class TestScenarioSupport {
    fun loadTestScenario(name: String) {
        val dataSource = DatabaseSupport().setupDatabase()
        val template = DatabaseTemplate(dataSource)
        this.javaClass.classLoader.getResourceAsStream("scenarios/" + name + ".sql").reader().readLines()
            .asSequence()
            .filterNot(String::isNullOrBlank)
            .forEach {
                template.execute(it)
            }
    }
}