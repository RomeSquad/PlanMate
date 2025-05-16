package org.example.logic.entity.auth

import java.util.*

data class User(
    val userId: UUID,
    val username: String,
    val userRole: UserRole
)

