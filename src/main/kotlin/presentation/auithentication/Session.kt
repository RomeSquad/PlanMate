package org.example.presentation.auithentication

import org.example.logic.entity.User

object Session {
    var currentUser: User? = null
    fun getUser(): User? {
        return currentUser
    }
}