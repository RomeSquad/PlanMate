package org.example.data.repository

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
        users += getAllUsers().getOrThrow()
    }

    override fun insertUser(username: String, password: String, userRole: UserRole): Result<User> {


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

    override fun loginUser(username: String, password: String): Result<User> {

        checkUserNameAndPassword(username, password).onFailure { return Result.failure(it) }
        validPassword(password).onFailure { return Result.failure(it) }

        val user = users.find { it.username == username } ?: return Result.failure(Exception("User not found"))
        return Result.success(user)

    }

    override fun getAllUsers(): Result<List<User>> {
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