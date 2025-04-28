package org.example.logic.repository

import org.example.logic.entity.User

interface AuthenticationRepository {

    fun getUser(username: String): User

    fun saveUser(user: User)

}