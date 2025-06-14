package org.example.logic.request

import org.example.logic.entity.auth.UserRole

data class CreateUserRequest(
    val username: String,
    val password: String,
    val userRole: UserRole,
)
