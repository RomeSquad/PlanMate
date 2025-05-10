package org.example.presentation.io

import org.example.presentation.menus.MenuAction

class ConsoleWriter : UiDisplayer {
    override fun displayMenu(options: List<MenuAction>) {
        options.forEachIndexed { index, option ->
            displayMessage("${index + 1}- ${option.description}")
        }
        displayPrompt("Choose the action from (1)..(${options.size}) or anything else to exit: ")
    }

    override fun displayMessage(message: String) = println(message)

    override fun displayError(message: String?) = println("Error: ${message ?: "Unknown error"}")

    override fun displayPrompt(prompt: String) {
        val yellow = "\u001B[33m"
        val reset = "\u001B[0m"
        print("$yellow$prompt$reset")
    }
}