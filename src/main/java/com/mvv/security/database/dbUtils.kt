package com.mvv.security.database

import java.sql.ResultSet


const val statementsSeparator = "---- \$statement separator\$ ----"

fun ResultSet.asIterable(): Iterable<ResultSet> = Iterable { ResultSetAsIterator(this) }

private class ResultSetAsIterator(val resultSet: ResultSet) : Iterator<ResultSet> {
    override fun hasNext(): Boolean = resultSet.next()
    override fun next(): ResultSet = resultSet
}


fun printTableQuery(title: String, rs: ResultSet, maxRowCount: Int) {
    val columnNames = mutableListOf<String>()
    for (i in 1..rs.metaData.columnCount) {
        columnNames.add(rs.metaData.getColumnName(i))
    }

    // INITIAL_SCHEMA
    println("-------------------------------------------------")
    println("            $title")
    println("-------------------------------------------------")

    for (i in 1..maxRowCount) {
        if (!rs.next()) break

        println("-------------------------------------------------")
        for (cn in columnNames) {
            val isVeryCaseSensitiveInfo = cn.contains("password", true) ||
                    cn.contains("passw", true) ||
                    cn.contains("pssw", true) ||
                    cn.contains("psw", true)
            val v = if (isVeryCaseSensitiveInfo) "*" else rs.getObject(cn)
            println("$cn: $v")
        }
    }
}
