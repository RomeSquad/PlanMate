package org.example.presentation.user.admin

import org.example.logic.usecase.auth.GetAllUsersUseCase
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import presentation.io.InputReader

class ViewAllUserUI(
    private val getAllUsersUseCase: GetAllUsersUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║     All Users Viewer     ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            val users = getAllUsersUseCase.getAllUsers()
            if (users.isEmpty()) {
                ui.displayMessage("❌ No users found.")
            } else {
                ui.displayMessage("✅ All Users:")
                users.forEachIndexed { index, user ->
                    ui.displayMessage(
                        """
                        User ${index + 1}:
                        ID: ${user.userId}
                        Username: ${user.username}
                        Role: ${user.userRole}
                        --------------------
                        """.trimIndent()
                    )
                }
            }

            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to retrieve users"}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}