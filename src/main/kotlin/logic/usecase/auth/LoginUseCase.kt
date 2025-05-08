package org.example.logic.usecase.auth

import org.example.logic.InvalidUserInputException
import org.example.logic.entity.auth.User
import org.example.logic.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun login(username: String, password: String): User {
        try {

            if (username.isBlank()) {
                throw InvalidUserInputException("Username cannot be empty")
            }

            if (password.isBlank()) {
                throw InvalidUserInputException("Password cannot be empty")
            }


            return authRepository.loginUser(username, password)

        } catch (e: IllegalArgumentException) {
            throw InvalidUserInputException("Invalid input: ${e.message}")
        }
    }

}