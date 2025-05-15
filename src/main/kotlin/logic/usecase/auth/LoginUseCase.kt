package org.example.logic.usecase.auth


import org.example.logic.entity.User
import org.example.logic.repository.AuthRepository
import org.example.logic.request.auth.LoginRequest

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun login(username: String, password: String): User {
        val request = LoginRequest(username, password)
        return authRepository.loginUser(request)
    }

}