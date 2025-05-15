package org.example.data.datasource.authentication

import kotlinx.coroutines.runBlocking
import org.example.logic.request.CreateUserRequest
import org.example.data.datasource.mapper.fromCsvRowToUser
import org.example.data.datasource.mapper.toCsvRow
import org.example.data.datasource.mapper.toUser
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileWriter
import org.example.data.utils.hashStringWithMD5
import org.example.logic.entity.auth.User
import org.example.logic.exception.UserNameAlreadyExistsException
import org.example.logic.exception.UserNotFoundException
import org.example.logic.request.LoginRequest
import java.io.File
import java.util.*

class CsvAuthDataSource(
    private val csvFileReader: CsvFileReader,
    private val csvFileWriter: CsvFileWriter,
    private val userFile: File
) : AuthDataSource {
    private var users = mutableListOf<User>()

    init {
        runBlocking {
            users += getAllUsers()
        }
    }

    override suspend fun insertUser(request: CreateUserRequest): User {
        isUserNameExists(request.username)
        val newUser = request.toUser()
        users.add(newUser)
        saveAllUsers()
        return newUser
    }

    override suspend fun loginUser(request: LoginRequest): User {
        val hashedPassword = hashStringWithMD5(request.password)
        val user = users.find { it.username == request.username && it.password == hashedPassword }
            ?: throw UserNotFoundException()
        return user
    }

    override suspend fun deleteUser(username: String): Boolean {
        val user = users.find { it.username == username } ?: throw UserNotFoundException()
        users.remove(user)
        return true
    }

    override suspend fun editUser(user: User) {
        val existingUser = users.find { it.username == user.username } ?: throw UserNotFoundException()
        users.remove(existingUser)
        users.add(user)
        saveAllUsers()
    }

    override suspend fun getUserByUserName(username: String): User? {
        return users.find { it.username == username }
    }

    fun saveAllUsers() {
        users.forEach { user ->
            val csvRow = user.toCsvRow()
            csvFileWriter.writeCsv(userFile, listOf(csvRow))
        }
    }

    override suspend fun isUserNameExists(username: String) {
        if (users.any { it.username == username }) {
            throw UserNameAlreadyExistsException()
        }
    }

    override suspend fun getCurrentUser(): User? {
        return users.firstOrNull()
    }

    override suspend fun getUserById(id: UUID): User? {
        return users.find { it.userId == id }
    }


    override suspend fun getAllUsers(): List<User> {
        val data = csvFileReader.readCsv(userFile)
        val users = data.map { it.fromCsvRowToUser() }
        return users
    }

}