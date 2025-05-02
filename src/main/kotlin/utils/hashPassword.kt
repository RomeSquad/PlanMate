package org.example.utils

import java.security.MessageDigest

fun hashPassword(password: String): String {
    val bytes = password.toByteArray()
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}