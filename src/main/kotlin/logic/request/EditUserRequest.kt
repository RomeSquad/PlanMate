package org.example.logic.request

import org.example.logic.entity.auth.UserRole

data class EditUserRequest(
    val username: String,
    val password: String,
    val userRole: UserRole
)
