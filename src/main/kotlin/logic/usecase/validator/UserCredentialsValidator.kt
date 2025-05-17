package logic.usecase.validator

interface UserCredentialsValidator {
    fun validateCredentialsNotEmpty(username: String, password: String)
    fun validatePasswordStrength(password: String)
    fun validateAll(username: String, password: String)

}