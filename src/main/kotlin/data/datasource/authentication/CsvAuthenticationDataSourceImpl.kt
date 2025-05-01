package org.example.data.datasource.authentication

import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole


class CsvAuthenticationDataSourceImpl : AuthenticationDataSource {

    private val users = mutableListOf<User>()
    private var nextUserId = 1

    override fun insertUser(username: String, password: String, userRole: UserRole): Result<User> {
        if (username.isEmpty()) {
            throw IllegalArgumentException("Username cannot be empty")
        }

        val user = object : User {
            override val userId: Int = nextUserId++
            override val username: String = username
            override val password: String = password
            override val userRole: UserRole = userRole
        }
        users.add(user)
        return Result.success(user)
    }

    override fun getUserById(userId: Int): Result<User> {
        if (userId < 0) {
            throw IllegalArgumentException("User ID cannot be negative")
        }
        return users.find { it.userId == userId }?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("User not found"))
    }

    override fun getUserByUsername(username: String): Result<User> {
        if (username.isEmpty()) {
            throw IllegalArgumentException("Username cannot be empty")
        }
        return users.find { it.username == username }?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException("User not found"))
    }
}