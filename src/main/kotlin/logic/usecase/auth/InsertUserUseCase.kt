package org.example.logic.usecase.auth

import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.AuthRepository

class InsertUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun insertUser(username: String, password: String, userRole: UserRole): Result<User> {
        return authRepository.insertUser(username, password, userRole)
    }

}