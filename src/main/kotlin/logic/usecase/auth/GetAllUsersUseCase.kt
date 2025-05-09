package org.example.logic.usecase.auth

import org.example.logic.entity.auth.User
import org.example.logic.repository.AuthRepository

class GetAllUsersUseCase(
    private val authenticationRepository: AuthRepository
) {
    suspend fun getAllUsers(): List<User> {
        return authenticationRepository.getAllUsers()
    }
}