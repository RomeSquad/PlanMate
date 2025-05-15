package org.example.logic.repository

import data.datasource.authentication.dto.UserDto
import org.example.logic.request.CreateUserRequest
import org.example.logic.entity.auth.User
import org.example.logic.request.EditUserRequest
import org.example.logic.request.LoginRequest
import java.util.*

interface AuthRepository {

    suspend fun insertUser(request: CreateUserRequest): User
    suspend fun loginUser(request: LoginRequest): User
    suspend fun getAllUsers(): List<User>
    suspend fun deleteUser(username: String): Boolean
    suspend fun editUser(request : EditUserRequest)
    suspend fun getUserByUserName(username: String): User?
    suspend fun getUserById(id: UUID): User?
    suspend fun getCurrentUser(): User?
}