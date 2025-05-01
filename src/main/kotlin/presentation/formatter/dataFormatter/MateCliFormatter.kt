package org.example.presentation.formatter.dataFormatter

import org.example.logic.entity.auth.Mate

fun Mate.format(): String {
    return String().format(
        "%-10s | %-15s | %-10s | %-10s",
        "ID: $userId",
        "Username: $username",
        "Pass: $password",
        "Role: $userRole"
    )
}