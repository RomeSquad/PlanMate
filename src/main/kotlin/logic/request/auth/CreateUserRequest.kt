package logic.request.auth

import org.example.logic.entity.User


data class CreateUserRequest(
    val username: String,
    val password: String,
    val userRole: User.UserRole,
)
