package org.example.logic.usecase.auth

import org.example.logic.entity.auth.User
import org.example.logic.repository.AuthRepository

class GetUserByUsernameUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun getUserByUsername(userName: String): User? {
        return authRepository.getUserByUserName(userName)
    }
}