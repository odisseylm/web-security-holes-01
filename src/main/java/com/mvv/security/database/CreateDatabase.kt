package com.mvv.security.database

import java.sql.Connection
import java.sql.DriverManager
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import kotlin.text.Charsets.UTF_8


class CreateDatabase {
    init { Class.forName("org.hsqldb.jdbc.JDBCDriver") }

    @Suppress("SqlSourceToSinkFlow")
    fun create() {
        openAdminConnection().use { con ->

            con.createStatement().use { st ->
                "/sql/admin.sql".getResourceText()
                    .split(statementsSeparator)
                    .forEach { st.execute(it) }
            }

            con.createStatement().use { st ->
                "/sql/ddl.sql".getResourceText()
                    .split(statementsSeparator)
                    .forEach { st.execute(it) }
            }

            con.createStatement().use { st ->
                "/sql/admin-02.sql".getResourceText()
                    .split(statementsSeparator)
                    .forEach { st.execute(it) }
            }

            con.createStatement().use { st -> st.execute("commit") }
        }
    }

    fun fillData() {
        fillDataUsers()
        fillDataAccounts()
    }

    @Suppress("SqlSourceToSinkFlow")
    fun recreateData() {
        val queries = listOf(" delete from ACCOUNT ", " delete from USER ")
        openBankSchemaConnection().use { con ->
            con.createStatement().use { st -> queries.forEach { st.execute(it) } } }
        fillData()
    }

    @Suppress("SqlSourceToSinkFlow")
    fun fillDataUsers() {
        openBankSchemaConnection().use { con ->
            con.createStatement().use { st ->
                "/sql/data-user.sql".getResourceText()
                    .split(statementsSeparator)
                    .forEach { st.execute(it) }
            }
        }
    }

    @Suppress("SqlSourceToSinkFlow")
    fun fillDataAccounts() {
        openBankSchemaConnection().use { con ->
            con.createStatement().use { st ->
                "/sql/data-account.sql".getResourceText()
                    .split(statementsSeparator)
                    .forEach { st.execute(it) }
            }
        }
    }

    @Suppress("ConstPropertyName")
    companion object {
        const val dataBaseId = "jdbc:hsqldb:mem:mymemdb"
    }
}


class DatabaseInitServletContextListener : ServletContextListener {
    override fun contextInitialized(sce: ServletContextEvent?) {
        CreateDatabase().also {
            it.create()
            it.fillData()
        }
    }
}


private fun String.getResourceText(): String {
    val resourceUrl = CreateDatabase::class.java.getResource(this)
    requireNotNull(resourceUrl) { "Resource [$resourceUrl] is not found." }

    return resourceUrl.openStream().use { String(it.readAllBytes(), UTF_8) }
}


fun openAdminConnection(): Connection =
    DriverManager.getConnection(CreateDatabase.dataBaseId, "SA", "SA")

fun openBankSchemaConnection(): Connection =
    DriverManager.getConnection(CreateDatabase.dataBaseId, "BANK_USER", "bankPassword1")