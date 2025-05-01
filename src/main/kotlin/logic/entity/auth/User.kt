package org.example.logic.entity.auth

interface User {

    val userId: Int
    val username: String
    val password: String
    val userRole: UserRole
        get() = UserRole.ADMIN
}

enum class UserRole {
    ADMIN, MATE
}



