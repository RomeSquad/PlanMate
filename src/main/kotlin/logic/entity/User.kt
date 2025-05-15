package org.example.logic.entity

import java.util.*


data class User(
    val userId: UUID = UUID.randomUUID(),
    val username: String,
    val password: String,
    val userRole: UserRole,
) {
    enum class UserRole {
        ADMIN, MATE
    }
}

