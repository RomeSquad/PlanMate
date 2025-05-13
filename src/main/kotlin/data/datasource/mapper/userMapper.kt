package org.example.data.datasource.mapper

import logic.request.auth.CreateUserRequest
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.utils.hashPassword
import java.util.*

fun User.toCsvRow(): List<String> {
    return listOf(userId.toString(), username, password, userRole.toString())
}


fun List<String>.fromCsvRowToUser(): User {
    return User(
        userId = UUID.fromString(this[0]),
        username = this[1],
        password = this[2],
        userRole = UserRole.valueOf(this[3])
    )
}

fun CreateUserRequest.toUser() = User(
    username = username,
    password = hashPassword(password),
    userRole = userRole
)