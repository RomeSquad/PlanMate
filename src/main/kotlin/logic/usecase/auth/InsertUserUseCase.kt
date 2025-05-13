package org.example.logic.usecase.auth

import logic.request.auth.CreateUserRequest
import logic.usecase.validator.UserCredentialsValidator
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.AuthRepository

class InsertUserUseCase(
    private val authRepository: AuthRepository,
    private val userCredentialsValidator: UserCredentialsValidator
) {
    suspend fun insertUser(username: String, password: String, userRole: UserRole): User {
        userCredentialsValidator.validateCredentialsNotEmpty(username, password)
        userCredentialsValidator.validatePasswordStrength(password)
        val request = CreateUserRequest(username, password, userRole)
        return authRepository.insertUser(request)
    }
}