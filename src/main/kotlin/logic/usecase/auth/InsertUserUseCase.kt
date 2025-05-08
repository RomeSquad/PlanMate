package org.example.logic.usecase.auth

import org.example.logic.InvalidUserInputException
import org.example.logic.UserAlreadyExistsException
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.AuthRepository

class InsertUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun insertUser(username: String, password: String, userRole: UserRole): User {
        try {

            if (username.isBlank()) {
                throw InvalidUserInputException("Username cannot be empty")
            }

            if (password.isBlank()) {
                throw InvalidUserInputException("Password cannot be empty")
            }


            return authRepository.insertUser(username, password, userRole)

        } catch (e: IllegalArgumentException) {
            throw InvalidUserInputException("Invalid input: ${e.message}")
        }
    }
}