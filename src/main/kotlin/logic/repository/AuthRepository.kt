package org.example.logic.repository

import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole

interface AuthRepository {

    suspend fun insertUser(username: String, password: String, userRole: UserRole): User
    suspend fun loginUser(username: String, password: String): User
    suspend fun getAllUsers():List<User>
}