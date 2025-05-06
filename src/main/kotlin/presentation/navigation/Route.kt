package org.example.presentation.navigation

sealed interface Route {
    data object MateScreen : Route
    data object AdminScreen : Route
    data object LoginScreen : Route
    data object RegisterScreen : Route
    data object EditProjectScreen : Route
    data object InsertProjectScreen : Route
    data object InsertUserScreen : Route
}