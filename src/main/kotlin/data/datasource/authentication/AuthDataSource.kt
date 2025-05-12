package org.example.data.datasource.authentication



import logic.request.auth.CreateUserRequest
import org.example.logic.entity.auth.User
import org.example.logic.request.auth.LoginRequest

interface AuthDataSource {
    suspend fun getAllUsers(): List<User>
    suspend fun insertUser(request: CreateUserRequest): User
    suspend fun loginUser(request: LoginRequest): User
    suspend fun deleteUser(username: String): Boolean
    suspend fun editUser(user: User)
    suspend fun getUserByUserName(username: String): User?
    suspend fun isUserNameExists(username: String)

    suspend fun getCurrentUser() : User?
}