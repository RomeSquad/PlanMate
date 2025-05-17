package org.example.presentation.user.admin

import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.usecase.auth.InsertUserUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class CreateUserUi(
    private val insertUserUseCase: InsertUserUseCase
) : MenuAction {
    override val description: String = buildDescription()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            val userInput = collectUserInput(ui, inputReader)
            if (!confirmUserCreation(ui, inputReader, userInput)) {
                ui.displayMessage("🛑 User creation canceled.")
                return
            }
            val user = createUser(userInput)
            ui.displayMessage("✅ User '${userInput.username}' created successfully with ID '${user.userId}'!")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Invalid input: ${e.message ?: "Invalid data provided"}")
        } catch (e: Exception) {
            ui.displayMessage("❌ Error: ${e.message ?: "Failed to create user"}")
        }
    }

    private fun buildDescription(): String = """
        ╔══════════════════════════╗
        ║    Create a New User     ║
        ╚══════════════════════════╝
        """.trimIndent()

    private fun collectUserInput(ui: UiDisplayer, inputReader: InputReader): UserInput {
        val username = readValidatedInput(ui, inputReader, "Username", "Username must not be blank") { it.isNotBlank() }
        val password = readValidatedInput(ui, inputReader, "Password", "Password must not be blank") { it.isNotBlank() }
        val userRole = selectUserRole(ui, inputReader)
        return UserInput(username, password, userRole)
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

    private fun selectUserRole(ui: UiDisplayer, inputReader: InputReader): UserRole {
        ui.displayMessage("🔹 Select User Role:")
        ui.displayMessage("1. ADMIN\n2. MATE")
        val choice = inputReader.readString("Role (1-2): ").trim().toIntOrNull()
        return when (choice) {
            1 -> UserRole.ADMIN
            2 -> UserRole.MATE
            else -> throw IllegalArgumentException("Invalid role selection. Choose 1 or 2.")
        }
    }

    private fun confirmUserCreation(ui: UiDisplayer, inputReader: InputReader, input: UserInput): Boolean {
        ui.displayMessage("⚠️ Create user '${input.username}' with role '${input.userRole}'? [y/n]")
        val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
        return confirmation == "y"
    }

    private suspend fun createUser(input: UserInput): User {
        return insertUserUseCase.insertUser(input.username, input.password, input.userRole)
    }

    private data class UserInput(
        val username: String,
        val password: String,
        val userRole: UserRole
    )
}