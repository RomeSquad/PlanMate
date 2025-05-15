package data.datasource.authentication.dto

import org.example.logic.entity.auth.UserRole
import java.util.UUID

data class UserDto(
    val userId: UUID = UUID.randomUUID(),
    val username: String,
    val password: String,
    val userRole: UserRole,
)