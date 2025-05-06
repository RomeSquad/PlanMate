package org.example.data.datasource.authentication


import org.example.logic.entity.auth.User

interface AuthDataSource {
    suspend fun getAllUsers(): Result<List<User>>
   suspend fun saveAllUsers(users: List<User>): Result<Unit>
}