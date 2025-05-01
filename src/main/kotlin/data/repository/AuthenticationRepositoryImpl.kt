package org.example.data.repository

import org.example.data.datasource.authentication.AuthenticationDataSource
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.AuthenticationRepository
import org.example.utils.hashPassword

class AuthenticationRepositoryImpl(
    private val authDataSource: AuthenticationDataSource
) : AuthenticationRepository {
    override fun registerUser(username: String, password: String, userRole: UserRole): Result<User> {
        val hashedPassword = hashPassword(password)

        // Check if the user already exists
        // Check if the username is not empty fun
        // Check if the password is not empty fun
        // Check if the password valid
        return authDataSource.insertUser(username, hashedPassword, userRole)
    }

    override fun loginUser(username: String, password: String): Result<User> {
        val hashedPassword = hashPassword(password)
        val userResult = authDataSource.getUserByUsername(username)


        // Check if the username is not empty fun
        // Check if the password is not empty fun


        return userResult.mapCatching { user ->
            if (user.password == hashedPassword) {
                user
            } else {
                throw IllegalArgumentException("Invalid credentials")
            }
        }
    }

    override fun getUserById(userId: Int): Result<User> {
        return authDataSource.getUserById(userId)
    }

}