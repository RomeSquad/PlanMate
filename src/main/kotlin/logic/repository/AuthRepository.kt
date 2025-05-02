package org.example.logic.repository

import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole

interface AuthRepository {

    fun insertUser(username: String, password: String, userRole: UserRole): Result<User>
    fun loginUser(username: String, password: String): Result<User>
    fun getAllUsers(): Result<List<User>>
}