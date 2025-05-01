package org.example.data.datasource.authentication

import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.data.utils.fromCsvRowToUser
import org.example.data.utils.toCsvRow
import org.example.logic.entity.auth.User
import java.io.File

class CsvAuthDataSource(
    private val csvFileReader: CsvFileReader,
    private val csvFileWriter: CsvFileWriter
) : AuthDataSource {

    private val userFile = File("users.csv")
    override fun getAllUsers(): Result<List<User>> {
        val data = csvFileReader.readCsv(userFile)
        println(data[0])
        val users = data.map { it.fromCsvRowToUser() }
        return Result.success(users)
    }

    override fun saveAllUsers(users: List<User>): Result<Unit> {
        users.forEach { user ->
            val csvRow = user.toCsvRow()
            csvFileWriter.writeCsv(userFile, listOf(csvRow))
        }
        return Result.success(Unit)
    }


}