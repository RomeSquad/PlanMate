package org.example.logic.request.auth

data class LoginRequest(
    val username: String,
    val password: String,
)