package org.example.data.datasource.authentication

import org.example.data.repository.mapper.fromCsvRowToUser
import org.example.data.repository.mapper.toCsvRow
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.logic.entity.auth.User
import java.io.File

class CsvAuthDataSource(
    private val csvFileReader: CsvFileReader,
    private val csvFileWriter: CsvFileWriter,
    private val userFile: File
) : AuthDataSource {

    override suspend fun getAllUsers(): List<User> {
        val data = csvFileReader.readCsv(userFile)
        val users = data.map { it.fromCsvRowToUser() }
        return users
    }

    override suspend fun saveAllUsers(users: List<User>) {
        users.forEach { user ->
            val csvRow = user.toCsvRow()
            csvFileWriter.writeCsv(userFile, listOf(csvRow))
        }
    }


}