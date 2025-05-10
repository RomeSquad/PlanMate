package org.example.presentation.auth

import org.example.logic.entity.auth.User

object Session {
    var currentUser: User? = null
    fun getUser(): User? {
        return currentUser
    }
}