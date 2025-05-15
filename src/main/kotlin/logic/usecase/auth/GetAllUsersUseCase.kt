package org.example.logic.usecase.auth

import org.example.logic.entity.User
import org.example.logic.repository.AuthRepository

class GetAllUsersUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun getAllUsers(): List<User> {
        return authRepository.getAllUsers()
    }
}