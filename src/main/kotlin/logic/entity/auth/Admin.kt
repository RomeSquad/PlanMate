package org.example.logic.entity.auth

data class Admin(
    override val userId: Int,
    override val username: String,
    override val password: String,
    override val userRole: UserRole = UserRole.ADMIN
) : User {
    override fun toString(): String {
        return String.format(
            "%-10s | %-15s | %-10s | %-10s",
            "ID: $userId",
            "Username: $username",
            "Pass: $password",
            "Role: $userRole"
        )
    }
}