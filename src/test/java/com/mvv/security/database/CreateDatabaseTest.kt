package com.mvv.security.database

import com.mvv.test.useAssertJSoftAssertions
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

import java.sql.DriverManager


@Suppress("SqlDialectInspection", "SqlNoDataSourceInspection")
class CreateDatabaseTest {

    @Test
    @Order(0)
    fun test() { useAssertJSoftAssertions {
        CreateDatabase().create()

        openAdminConnection().use { con ->
            con.createStatement().use { st ->
                st.executeQuery(" select * from INFORMATION_SCHEMA.SYSTEM_USERS ").use { rs ->
                    println("l: " + rs.asIterable().map { it.getObject(1) })
                }

                st.executeQuery(" select * from INFORMATION_SCHEMA.SYSTEM_USERS ")
                    .use { rs -> printTableQuery("SYSTEM_USERS", rs, 50) }
            }
        }


        openBankSchemaConnection().use { con ->
            con.createStatement().use { st ->
                st.executeQuery(" select * from INFORMATION_SCHEMA.SYSTEM_USERS ")
                    .use { rs -> printTableQuery("SYSTEM_USERS", rs, 50) }

                st.executeQuery(" select count(*) as User_COUNT from USER ")
                    .use { rs -> printTableQuery("USER count", rs, 50) }

                st.executeQuery(" select * from USER ")
                    .use { rs -> printTableQuery("USERS", rs, 50) }
            }
        }


        CreateDatabase().fillData()

        DriverManager.getConnection(CreateDatabase.dataBaseId, "BANK_USER", "bankPassword1").use { con ->
            con.createStatement().use { st ->

                st.executeQuery(" select count(*) as User_COUNT from USER ")
                    .use { rs -> printTableQuery("USER count", rs, 50) }
                st.executeQuery(" select * from USER ")
                    .use { rs -> printTableQuery("USER", rs, 50) }

                st.executeQuery(" select * from ACCOUNT ")
                    .use { rs -> printTableQuery("ACCOUNT", rs, 50) }
            }
        }


        run {
            val accounts = UnsafeSQLService().getAccountsByNameLike("main")
            assertThat(accounts).hasSize(1)
        }

        run {
            val accounts = SafeSQLService().getAccountsByNameLike("")
            assertThat(accounts).hasSize(2)
        }
    } }


    @Test
    @Order(2)
    fun injectionTest_removeAllData() { useAssertJSoftAssertions {

        makeSureDatabaseIsCreatedAndFilled()

        val unsafeSQLService = UnsafeSQLService()

        run {
            val accounts = unsafeSQLService.getAccountsByNameLike("")
            assertThat(accounts).isNotEmpty
            println("Accounts = ${accounts.size} before attempt to delete them.")
        }

        run {
            val accounts = unsafeSQLService.getAccountsByNameLike("main'; delete from ACCOUNT; select * from ACCOUNT where '1' = '")
            assertThat(accounts).isEmpty()
        }

        run {
            val accounts = unsafeSQLService.getAccountsByNameLike("")
            println("Accounts = ${accounts.size} after attempt to delete them.")
            assertThat(accounts).isEmpty()
        }
    } }


    @Test
    @Order(2)
    fun injectionTest_stealAccountOfAllUsers() { useAssertJSoftAssertions {

        makeSureDatabaseIsCreatedAndFilled()

        val unsafeSQLService = UnsafeSQLService()

        run {
            val accounts = unsafeSQLService.getAccountsByNameLike("")
            println("Accounts = ${accounts.size} of your user.")
            assertThat(accounts).hasSize(2)
        }

        run {
            val accounts = unsafeSQLService.getAccountsByNameLike("main'; select * from ACCOUNT where 1=1 or '1' = '")
            println("Accounts = ${accounts.size} of all users.")
            assertThat(accounts).hasSize(3)
        }
    } }

    @Test
    @Order(99)
    fun usageOfDatabaseInitServletContextListener() {
        try { DatabaseInitServletContextListener() } catch (_: Exception) { }
    }


    private fun makeSureDatabaseIsCreatedAndFilled() {
        val tableAccountExists = openAdminConnection().use { con ->
            con.createStatement().use { st ->
                st.executeQuery(" select * from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'ACCOUNT' ").use { it.next() } }
        }

        if (!tableAccountExists) {
            println("Tables are not created yet. Lets create them.")
            val database = CreateDatabase()
            database.create()
            database.fillData()
        }

        val tableAccountContainsData = openBankSchemaConnection().use { con ->
            con.createStatement().use { st -> st.executeQuery(" select * from ACCOUNT ").use { it.next() } }
        }

        if (!tableAccountContainsData) {
            CreateDatabase().fillDataAccounts()
        }
    }
}
