package org.example.data.datasource.authentication

import org.example.logic.entity.Admin
import org.example.logic.entity.Mate
import org.example.logic.entity.User

class CsvAuthenticationDataSource : AuthenticationDataSource {

    val fileName: String = "user.csv"

    val fileHeader: List<String> = listOf("username", "password", "role")


    override fun saveUser(user: User) {
        when (user) {
            is Admin -> {

            }

            is Mate -> {

            }

            else -> {
                throw Exception()
            }
        }
    }

    override fun getUser(username: String): User {
        TODO()
    }
}