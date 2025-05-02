package org.example.logic.entity.auth


data class User(
     val userId: Int,
     val username: String,
     val password: String,
     val userRole: UserRole,
)

