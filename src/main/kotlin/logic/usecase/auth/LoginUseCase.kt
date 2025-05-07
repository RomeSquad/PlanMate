package org.example.logic.usecase.auth

import org.example.logic.entity.auth.User
import org.example.logic.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun login(username: String, password: String): User {
        return authRepository.loginUser(username, password)
    }

}