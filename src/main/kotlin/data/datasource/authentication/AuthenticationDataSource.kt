package org.example.data.datasource.authentication

import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole

interface AuthenticationDataSource {
    fun insertUser(username: String, password: String, userRole: UserRole): Result<User>
    fun getUserById(userId: Int): Result<User>
    fun getUserByUsername(username: String): Result<User>
}