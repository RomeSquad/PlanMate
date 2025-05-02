package org.example.logic.usecase

import org.example.logic.entity.auth.User
import org.example.logic.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    fun login(username: String, password: String): Result<User> {
        return authRepository.loginUser(username, password)
    }

}