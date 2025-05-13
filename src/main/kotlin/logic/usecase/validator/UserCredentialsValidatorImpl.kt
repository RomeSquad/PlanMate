package logic.usecase.validator

import org.example.logic.exception.PasswordLengthException
import org.example.logic.exception.UserNameOrPasswordEmptyException

class UserCredentialsValidatorImpl : UserCredentialsValidator {
    override fun validateCredentialsNotEmpty(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) throw UserNameOrPasswordEmptyException()
    }

    override fun validatePasswordStrength(password: String) {
        if (password.length <= 6) throw PasswordLengthException()
    }
}