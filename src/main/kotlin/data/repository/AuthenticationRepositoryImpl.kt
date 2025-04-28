package org.example.data.repository

import org.example.data.datasource.authentication.CsvAuthenticationDataSource
import org.example.logic.entity.User
import org.example.logic.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val csvAuthenticationDataSource : CsvAuthenticationDataSource
) : AuthenticationRepository {


    override fun getUser(username: String): User {
       return csvAuthenticationDataSource.getUser(username)
    }

    override fun saveUser(user: User) {
         csvAuthenticationDataSource.saveUser(user)
    }
}