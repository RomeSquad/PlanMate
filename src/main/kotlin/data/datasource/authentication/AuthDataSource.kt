package org.example.data.datasource.authentication


import data.datasource.authentication.dto.UserDto
import org.example.logic.request.CreateUserRequest
import org.example.logic.request.EditUserRequest
import org.example.logic.request.LoginRequest
import java.util.*

interface AuthDataSource {
    suspend fun getAllUsers(): List<UserDto>
    suspend fun insertUser(request: CreateUserRequest): UserDto
    suspend fun loginUser(request: LoginRequest): UserDto
    suspend fun deleteUser(username: String): Boolean
    suspend fun editUser(request : EditUserRequest)
    suspend fun getUserByUserName(username: String): UserDto?
    suspend fun isUserNameExists(username: String)
    suspend fun getCurrentUser(): UserDto?
    suspend fun getUserById(id: UUID): UserDto?
}