package org.example.logic

class TaskNotFoundException(message: String) : Exception(message)
class EntityNotChangedException : Exception("Entity not changed")
class EmptyNameException : Exception("Name cannot be empty")
class EmptyPasswordException : Exception("Password cannot be empty")
class InvalidCredentialsException(message: String = "Invalid credentials") : Exception(message)