package org.example.logic.usecase.auth

import org.example.logic.entity.User
import org.example.logic.repository.AuthRepository

class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun getCurrentUser(): User? = authRepository.getCurrentUser()
}