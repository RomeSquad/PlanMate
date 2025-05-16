package org.example.logic.usecase.auth

import org.example.logic.entity.auth.User
import org.example.logic.exception.InvalidCredentialsException
import org.example.logic.exception.PasswordLengthException
import org.example.logic.exception.UserNameOrPasswordEmptyException
import org.example.logic.repository.AuthRepository
import org.example.logic.request.LoginRequest

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun login(username: String, password: String): User {
        validateLoginInputs(username, password)
        val request = LoginRequest(
            username = username,
            password = password
        )
        return try {
            authRepository.loginUser(request)
        } catch (e: Exception) {
            throw InvalidCredentialsException()
        }
    }

    private fun validateLoginInputs(username: String, password: String) {
        if (username.trim().isEmpty()) {
            throw UserNameOrPasswordEmptyException()
        }
        if (password.trim().isEmpty()) {
            throw UserNameOrPasswordEmptyException()
        }
        if (password.length < 6) {
            throw PasswordLengthException()
        }
    }
}