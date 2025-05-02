package org.example.logic.entity.auth

data class Admin(
    override val userId: Int,
    override val username: String,
    override val password: String,
    override val userRole: UserRole = UserRole.ADMIN
) : IUser