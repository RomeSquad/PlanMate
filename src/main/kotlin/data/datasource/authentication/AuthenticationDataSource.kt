package org.example.data.datasource.authentication

import org.example.logic.entity.User

interface AuthenticationDataSource {
    fun saveUser(user: User)
    fun getUser(username: String): User
}