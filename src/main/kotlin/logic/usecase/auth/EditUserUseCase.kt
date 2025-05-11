package org.example.logic.usecase.auth


import org.example.logic.entity.auth.User
import org.example.logic.exception.EmptyNameException
import org.example.logic.exception.EmptyPasswordException
import org.example.logic.exception.EntityNotChangedException
import org.example.logic.repository.AuthRepository


class EditUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun editUser(newUser: User, oldUser: User) {
        validateUserInputs(
            newUser = newUser,
            oldUser = oldUser
        )
        authRepository.editUser(user = newUser)
    }

    private fun validateUserInputs(newUser: User, oldUser: User) {
        if (newUser == oldUser)
            throw EntityNotChangedException()
        if (newUser.username.isEmpty()) throw EmptyNameException()
        if (newUser.password.isBlank()) throw EmptyPasswordException()
    }

}

