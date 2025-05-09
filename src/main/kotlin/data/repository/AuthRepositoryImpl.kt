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
            users += getAllUsers()
        }
    }

    override suspend fun insertUser(username: String, password: String, userRole: UserRole): User {
        checkUserNameAndPassword(username, password)
        validPassword(password)

        val hashedPassword = hashPassword(password)

        if (users.any { it.username == username }) {
            throw Exception("Username already exists")
        }

        val newUser = User(userId = users.size + 1, username = username, password = hashedPassword, userRole = userRole)
        users.add(newUser)
        authDataSource.saveAllUsers(users)
        return newUser
    }

    override suspend fun loginUser(username: String, password: String): User {

        checkUserNameAndPassword(username, password)
        validPassword(password)

        val hashedPassword = hashPassword(password)

        val user =
            users.find { it.username == username && it.password == hashedPassword } ?: throw Exception("User not found")
        return user

    }

    override suspend fun getAllUsers(): List<User> {
        return authDataSource.getAllUsers()
    }

    override suspend fun deleteUser(username: String): Boolean {
        val user = users.find { it.username == username } ?: throw Exception("User not found")
        users.remove(user)
        authDataSource.saveAllUsers(users)
        return true
    }

    override suspend fun editUser(user: User) {

    }

    override suspend fun getUserByUserName(username: String): User? {
        return users.find { it.username == username }
    }


    private fun checkUserNameAndPassword(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) throw Exception("Username or password cannot be empty")
    }

    private fun validPassword(password: String) {
        if (password.length <= 6) throw Exception("Password must be at least 6 characters")
    }

}