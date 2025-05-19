package org.example.presentation.auithentication

import org.example.logic.entity.auth.User
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.auth.LoginUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class LoginManagementUI(
    private val loginUseCase: LoginUseCase,
    private val mainMenuUI: MainMenuUI,
) : MenuAction {
    override val description: String = buildDescription()
    override val menu: Menu = Menu()

    private val menuOptions = listOf(
        MenuOption(1, "Login", ::handleLogin, "🔑"),
        MenuOption(2, "Exit", { _, _ -> throw ExitApplicationException() }, "🚪")
    )

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runMenuLoop(ui, inputReader, menuOptions, description) { choice ->
            choice == menuOptions.last().id // Exit option
        }
    }

    private fun buildDescription(): String = """
        ╔══════════════════════════╗
        ║       Login Menu         ║
        ╚══════════════════════════╝
        """.trimIndent()

    private suspend fun runMenuLoop(
        ui: UiDisplayer,
        inputReader: InputReader,
        options: List<MenuOption>,
        description: String,
        isExitOption: (Int) -> Boolean
    ) {
        while (true) {
            try {
                ui.displayMessage(description)
                displayMenuOptions(ui, options)
                val choice = readUserChoice(inputReader, options)
                if (isExitOption(choice)) return
                executeMenuAction(ui, inputReader, options, choice)
            } catch (e: ExitApplicationException) {
                ui.displayMessage("🚪 Exiting...")
                throw e // Propagate to App
            } catch (e: IllegalArgumentException) {
                ui.displayMessage("❌ Invalid input: ${e.message ?: "Please enter a valid number"}")
            } catch (e: Exception) {
                ui.displayMessage("❌ Error: ${e.message ?: "Failed to execute action"}")
            }
        }
    }

    private fun displayMenuOptions(ui: UiDisplayer, options: List<MenuOption>) {
        val menuText = options.joinToString("\n") { "${it.icon} ${it.id}. ${it.description}" } +
                "\n🔹 Choose an option (1-${options.size}):"
        ui.displayMessage(menuText)
    }

    private fun readUserChoice(inputReader: InputReader, options: List<MenuOption>): Int {
        val choice = inputReader.readString("Choice: ").trim().toIntOrNull()
        if (choice == null || choice !in options.map { it.id }) {
            throw IllegalArgumentException("Please select a number between 1 and ${options.size}")
        }
        return choice
    }

    private suspend fun executeMenuAction(
        ui: UiDisplayer,
        inputReader: InputReader,
        options: List<MenuOption>,
        choice: Int
    ) {
        val action = options.find { it.id == choice }?.action
            ?: throw IllegalStateException("Invalid menu option selected")
        action(ui, inputReader)
    }

    private suspend fun handleLogin(ui: UiDisplayer, inputReader: InputReader) {
        try {
            val credentials = collectCredentials(ui, inputReader)
            val user = performLogin(credentials)
            ui.displayMessage("🎉 Login successful! Welcome, ${user.username} (${user.userRole}).")
            mainMenuUI.execute(ui, inputReader)
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Invalid input: ${e.message ?: "Invalid credentials"}")
        } catch (e: Exception) {
            ui.displayMessage("❌ Error: ${e.message ?: "Failed to login"}")
        }
    }

    private fun collectCredentials(ui: UiDisplayer, inputReader: InputReader): Credentials {
        val username = readValidatedInput(ui, inputReader, "Username", "Username cannot be empty") { it.isNotBlank() }
        val password = readValidatedInput(ui, inputReader, "Password", "Password cannot be empty") { it.isNotBlank() }
        return Credentials(username, password)
    }

    private fun readValidatedInput(
        ui: UiDisplayer,
        inputReader: InputReader,
        field: String,
        errorMessage: String,
        validator: (String) -> Boolean
    ): String {
        ui.displayMessage("🔹 Enter $field:")
        val input = inputReader.readString("$field: ").trim()
        if (!validator(input)) throw IllegalArgumentException(errorMessage)
        return input
    }

    private suspend fun performLogin(credentials: Credentials): User {
        val user = loginUseCase.login(credentials.username, credentials.password)
        return user
    }

    private data class MenuOption(
        val id: Int,
        val description: String,
        val action: suspend (UiDisplayer, InputReader) -> Unit,
        val icon: String
    )

    private data class Credentials(
        val username: String,
        val password: String
    )

    private class ExitApplicationException : Exception("User requested application exit")
}