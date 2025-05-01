package org.example.logic.usecase

import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.AuthenticationRepository

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    fun login(username: String, password: String): Result<User> {
        return authenticationRepository.loginUser(username, password)
    }

}