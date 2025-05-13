package org.example.logic.exception

class UserNameOrPasswordEmptyException : IllegalArgumentException("Username or password cannot be empty")
class PasswordLengthException : IllegalArgumentException("Password must be at least 6 characters")
class UserNameAlreadyExistsException : IllegalArgumentException("Username already exists")
class UserNotFoundException : Exception("User not found")

open class UserException : Exception()
class InvalidUserException : UserException()
class EmptyTitleException : IllegalArgumentException()

class NotAccessException(message: String) : Exception(message)
class CantAddStateWithNoNameException(message: String) : Exception(message)



class TaskNotFoundException(message: String) : Exception(message)
class EntityNotChangedException(message: String) : Exception(message)
class EmptyNameException(message: String) : Exception(message)
class EmptyPasswordException(message: String) : Exception(message)
class InvalidCredentialsException(message: String = "Invalid credentials") : Exception(message)