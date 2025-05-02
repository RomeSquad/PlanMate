package org.example.data.utils

import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole

fun User.toCsvRow(): List<String> {
    return listOf(userId.toString(), username, password, userRole.toString())
}


fun List<String>.fromCsvRowToUser(): User {
    return User(
        userId = this[0].toInt(),
        username = this[1],
        password = this[2],
        userRole = UserRole.valueOf(this[3])
    )
}