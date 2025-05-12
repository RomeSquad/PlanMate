package org.example.presentation.utils.io

import org.example.presentation.utils.menus.MenuAction

interface UiDisplayer {
    fun displayMenu(options: List<MenuAction>)
    fun displayMessage(message: String)
    fun displayError(message: String?)
    fun displayPrompt(prompt: String): String
}



