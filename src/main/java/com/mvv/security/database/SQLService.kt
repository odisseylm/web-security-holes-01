package com.mvv.security.database

import java.math.BigDecimal
import java.sql.ResultSet
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse


//data class User (
//    val id: Long,
//    val firstName: String,
//    val lastName: String,
//)

data class Account (
    val id: Long,
    val userId: Long,
    val name: String,
    val amount: BigDecimal,
)


fun getCurrentUserId(): Long = 1L


interface SQLService {
    fun getAccountsByNameLike(namePart: String): List<Account>

    fun getAccountsByNameLike(request: HttpServletRequest, response: HttpServletResponse) {
        val accountNamePart = request.getParameter("accountNamePart")
        if (accountNamePart.isBlank()) {
            response.status = 400
            response.writer.write("Error: No accountNamePart.")
            return
        }

        val accounts = getAccountsByNameLike(accountNamePart)
        val asStr = accounts.joinToString("\n") { " ${it.name}    ${it.amount} " }
        response.writer.write(asStr)
    }

    fun recreateData() = CreateDatabase().recreateData()
}


@Suppress("SqlDialectInspection")
class UnsafeSQLService : SQLService {
    override fun getAccountsByNameLike(namePart: String): List<Account> {
        val currentUserId = getCurrentUserId()
        val accounts = openBankSchemaConnection().use { con ->
            val query = " select * from ACCOUNT a where a.USER = $currentUserId and lower(a.NAME) like '%${namePart.lowercase()}%' "
            println("Unsafe query: $query")
            con.createStatement()
                    .executeQuery(query)
                        .use { rs -> rs.toAccounts() }
        }

        return accounts
    }
}



@Suppress("SqlDialectInspection")
class SafeSQLService : SQLService {
    override fun getAccountsByNameLike(namePart: String): List<Account> {
        val currentUserId = getCurrentUserId()
        val accounts = openBankSchemaConnection().use { con ->
                con.prepareStatement(" select * from ACCOUNT a where a.USER = ? and lower(a.NAME) like ? ").use { ps ->
                    ps.setLong(1, currentUserId)
                    ps.setString(2, "%${namePart.lowercase()}%")

                    ps.executeQuery().use { rs -> rs.toAccounts() }
                }
        }

        return accounts
    }
}


private fun ResultSet.toAccounts() = this.asIterable().map {
    Account(
        it.getLong("ID"),
        it.getLong("USER"),
        it.getString("NAME"),
        it.getBigDecimal("AMOUNT"),
    )
}
