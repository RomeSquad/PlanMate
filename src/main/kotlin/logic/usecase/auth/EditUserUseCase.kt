package org.example.logic.usecase.auth

import org.example.logic.entity.auth.User
import org.example.logic.repository.AuthRepository


class EditUserUseCase(
    private val authenticationRepository: AuthRepository
) {
    suspend fun editUser(newUser: User, oldUser: User) {
        validateUserInputs(
            newUser = newUser,
            oldUser = oldUser
        )
        authenticationRepository.editUser(user = newUser)
    }

    private fun validateUserInputs(newUser: User, oldUser: User) {
        if (newUser == oldUser)
            throw EntityNotChangedException()
        if (newUser.username.isEmpty()) throw EmptyNameException()
        if (newUser.password.isBlank()) throw EmptyPasswordException()
    }

}

class EntityNotChangedException : Exception("Entity not changed")
class EmptyNameException : Exception("Name cannot be empty")
class EmptyPasswordException : Exception("Password cannot be empty")