package io.milk.database

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDate
import java.util.*
import javax.sql.DataSource

class DatabaseTemplate(val dataSource: DataSource) {

    fun <T> create(sql: String, id: (Long) -> T, vararg params: Any) =
        dataSource.connection.use { connection ->
            create(connection, sql, id, *params)
        }

    fun <T> create(connection: Connection, sql: String, id: (Long) -> T, vararg params: Any): T {
        return connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { statement ->
            setParameters(params, statement)
            statement.executeUpdate()
            val keys = statement.generatedKeys
            keys.next()
            id(keys.getLong(1))
        }
    }

    fun <T> findAll(sql: String, mapper: (ResultSet) -> T) = query(sql, {}, mapper)

    fun <T> findBy(sql: String, mapper: (ResultSet) -> T, id: Long): T? {
        val list = query(sql, { ps -> ps.setLong(1, id) }, mapper)
        when {
            list.isEmpty() -> return null

            else -> return list.first()
        }
    }

    fun update(sql: String, vararg params: Any) {
        dataSource.connection.use { connection ->
            update(connection, sql, *params)
        }
    }

    fun update(connection: Connection, sql: String, vararg params: Any) {
        return connection.prepareStatement(sql).use { statement ->
            setParameters(params, statement)
            statement.executeUpdate()
        }
    }

    private fun setParameters(params: Array<out Any>, statement: PreparedStatement) {
        for (i in params.indices) {
            val param = params[i]
            val parameterIndex = i + 1

            when (param) {
                is String -> statement.setString(parameterIndex, param)
                is Int -> statement.setInt(parameterIndex, param)
                is Long -> statement.setLong(parameterIndex, param)
                is Boolean -> statement.setBoolean(parameterIndex, param)
                is LocalDate -> statement.setDate(parameterIndex, Date.valueOf(param))

            }
        }
    }

    /// USED FOR TESTING

    fun execute(sql: String) {
        dataSource.connection.use { connection ->
            connection.prepareCall(sql).use(CallableStatement::execute)
        }
    }

    fun <T> query(sql: String, params: (PreparedStatement) -> Unit, mapper: (ResultSet) -> T): List<T> {
        val results = ArrayList<T>()

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { statement ->
                params(statement)
                statement.executeQuery().use { rs ->
                    while (rs.next()) {
                        results.add(mapper(rs))
                    }
                }
            }
        }
        return results
    }
}