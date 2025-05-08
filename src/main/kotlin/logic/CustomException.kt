package org.example.logic

import java.lang.Exception

class TaskNotFoundException(message: String): Exception(message)
class UserAlreadyExistsException(message: String): Exception(message)
class UserNotFoundException(message: String): Exception(message)
class InvalidUserInputException(message: String) : Exception(message)
