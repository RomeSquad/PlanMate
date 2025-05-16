package org.example.logic.usecase.auth


import data.datasource.authentication.dto.UserDto
import logic.usecase.validator.UserCredentialsValidator
import org.example.logic.entity.auth.User
import org.example.logic.exception.EmptyNameException
import org.example.logic.exception.EmptyPasswordException
import org.example.logic.exception.EntityNotChangedException
import org.example.logic.repository.AuthRepository
import org.example.logic.request.EditUserRequest


class EditUserUseCase(
    private val authRepository: AuthRepository,
    private val userCredentialsValidator: UserCredentialsValidator
) {
    suspend fun editUser(
        request : EditUserRequest
    ) {
        userCredentialsValidator.validatePasswordStrength(request.password)

        authRepository.editUser(request = request)
    }
}