package org.example.data.repository

import kotlinx.coroutines.runBlocking
import org.example.data.datasource.authentication.AuthDataSource
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.AuthRepository
import org.example.utils.hashPassword

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource
) : AuthRepository {

    private var users = mutableListOf<User>()

    init {
        runBlocking {
            users += getAllUsers().getOrThrow()
        }
    }

    override suspend fun insertUser(username: String, password: String, userRole: UserRole): Result<User> {


        checkUserNameAndPassword(username, password).onFailure { return Result.failure(it) }
        validPassword(password).onFailure { return Result.failure(it) }

        val hashedPassword = hashPassword(password)

        if (users.any { it.username == username }) {
            return Result.failure(Exception("Username already exists"))
        }

        val newUser = User(userId = users.size + 1, username = username, password = hashedPassword, userRole = userRole)
        users.add(newUser)
        return authDataSource.saveAllUsers(users).map { newUser }
    }

    override suspend fun loginUser(username: String, password: String): Result<User> {

        checkUserNameAndPassword(username, password).onFailure { return Result.failure(it) }
        validPassword(password).onFailure { return Result.failure(it) }

        val hashedPassword = hashPassword(password)

        val user = users.find { it.username == username && it.password == hashedPassword } ?: return Result.failure(Exception("User not found"))
        return Result.success(user)

    }

    override suspend fun getAllUsers(): Result<List<User>> {
        return authDataSource.getAllUsers()
    }


    private fun checkUserNameAndPassword(username: String, password: String): Result<Unit> {
        return if (username.isNotEmpty() && password.isNotEmpty()) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Username or password cannot be empty"))
        }
    }

    private fun validPassword(password: String): Result<Unit> {
        return if (password.length >= 6) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Password must be at least 6 characters"))
        }
    }

}