package org.example.logic.usecase

import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.AuthenticationRepository

class RegisterUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    fun register(username: String, password: String, userRole: UserRole): Result<User> {
        return authenticationRepository.registerUser(username, password, userRole)
    }

}