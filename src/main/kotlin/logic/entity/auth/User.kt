package org.example.logic.entity.auth


data class User(
    override val userId: Int,
    override val username: String,
    override val password: String,
    override val userRole: UserRole,
) : IUser

interface IUser {
    val userId: Int
    val username: String
    val password: String
    val userRole: UserRole
}





