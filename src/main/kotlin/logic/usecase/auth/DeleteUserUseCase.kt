package org.example.logic.usecase.auth

import org.example.logic.repository.AuthRepository

class DeleteUserUseCase(
    private val authenticationRepository: AuthRepository
) {
    suspend fun deleteUser(username: String): Boolean {
        return authenticationRepository.deleteUser(username)
    }
}