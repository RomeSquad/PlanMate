package org.example.data.datasource.authentication

import org.example.logic.entity.auth.User

interface AuthDataSource {

    fun getAllUsers(): Result<List<User>>
    fun saveAllUsers(users: List<User>): Result<Unit>
}