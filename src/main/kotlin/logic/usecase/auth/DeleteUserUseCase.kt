package org.example.logic.usecase.auth

import org.example.logic.repository.AuthRepository

class DeleteUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun deleteUser(username: String): Boolean {
        return authRepository.deleteUser(username)
    }
}