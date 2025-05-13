package org.example.data.repository

import logic.request.auth.CreateUserRequest
import org.example.data.datasource.authentication.AuthDataSource
import org.example.data.datasource.mapper.toUser
import org.example.logic.entity.auth.User
import org.example.logic.repository.AuthRepository
import org.example.logic.request.auth.LoginRequest
import java.util.*

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource
) : AuthRepository {

    override suspend fun insertUser(request: CreateUserRequest): User = authDataSource.insertUser(request).toUser()

    override suspend fun loginUser(request: LoginRequest): User = authDataSource.loginUser(request).toUser()

    override suspend fun getAllUsers(): List<User> = authDataSource.getAllUsers().map{ it.toUser() }

    override suspend fun deleteUser(username: String): Boolean = authDataSource.deleteUser(username)

    override suspend fun editUser(user: User) = authDataSource.editUser(user)

    override suspend fun getUserByUserName(username: String): User? = authDataSource.getUserByUserName(username)?.toUser()

    override suspend fun getUserById(id: UUID): User? = authDataSource.getUserById(id)?.toUser()

    override suspend fun getCurrentUser(): User? = authDataSource.getCurrentUser()?.toUser()

}