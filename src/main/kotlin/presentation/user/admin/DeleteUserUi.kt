package org.example.presentation.user.admin

import org.example.logic.usecase.auth.DeleteUserUseCase
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import org.example.presentation.utils.io.InputReader

class DeleteUserUi(
    private val deleteUserUseCase: DeleteUserUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║     Delete a User        ║
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

            ui.displayMessage("⚠️ Delete user '$username'? [y/n]")
            val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
            if (confirmation != "y") {
                ui.displayMessage("🛑 User deletion canceled.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }

            val deleted = deleteUserUseCase.deleteUser(username)
            if (deleted) {
                ui.displayMessage("✅ User '$username' deleted successfully!")
            } else {
                ui.displayMessage("❌ No user found with username '$username'.")
            }
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to delete user"}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}