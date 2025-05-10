package org.example.presentation.user.admin

import org.example.logic.entity.auth.UserRole
import org.example.logic.usecase.auth.InsertUserUseCase
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import presentation.io.InputReader

class CreateUserUi(
    private val insertUserUseCase: InsertUserUseCase
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║    Create a New User     ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Enter Username:")
            val username = inputReader.readString("Username: ").trim()
            if (username.isBlank()) {
                throw IllegalArgumentException("Username must not be blank")
            }

            ui.displayMessage("🔹 Enter Password:")
            val password = inputReader.readString("Password: ").trim()
            if (password.isBlank()) {
                throw IllegalArgumentException("Password must not be blank")
            }

            ui.displayMessage("🔹 Select User Role:")
            ui.displayMessage("1. ADMIN\n2. MATE")
            val roleChoice = inputReader.readString("Role (1-2): ").trim().toIntOrNull()
            val userRole = when (roleChoice) {
                1 -> UserRole.ADMIN
                2 -> UserRole.MATE
                else -> throw IllegalArgumentException("Invalid role selection. Choose 1 or 2.")
            }

            ui.displayMessage("⚠️ Create user '$username' with role '$userRole'? [y/n]")
            val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
            if (confirmation != "y") {
                ui.displayMessage("🛑 User creation canceled.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }

            val user = insertUserUseCase.insertUser(username, password, userRole)
            ui.displayMessage("✅ User '$username' created successfully with ID '${user.userId}'!")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to create user"}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}