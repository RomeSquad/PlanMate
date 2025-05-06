package org.example.data.datasource.authentication

import org.example.logic.entity.auth.User

class MongoAuthDataSource : AuthDataSource {
    override suspend fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun saveAllUsers(users: List<User>) {
        TODO("Not yet implemented")
    }
}