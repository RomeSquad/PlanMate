package org.example.logic.entity

interface User {

    val username: String
    val password: String
    val userRole: UserRole
        get() = UserRole.ADMIN

    fun canCreateProject(): Boolean
}

enum class UserRole {
    ADMIN, MATE
}


data class Admin(
    override val username: String,
    override val password: String
) : User {
    override fun canCreateProject() = true
}

data class Mate(
    override val username: String,
    override val password: String,
    override val userRole: UserRole = UserRole.MATE
) : User {
    override fun canCreateProject() = false
}
