package org.example.presentation.user.admin

import org.example.logic.entity.auth.User
import org.example.logic.usecase.auth.GetAllUsersUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class ViewAllUserUI(
    private val getAllUsersUseCase: GetAllUsersUseCase
) : MenuAction {
    override val description: String = buildDescription()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            val users = fetchUsers(ui)
            displayUsers(ui, users)
        } catch (e: Exception) {
            ui.displayMessage("❌ Error: ${e.message ?: "Failed to retrieve users"}")
        }
    }

    private fun buildDescription(): String = """
        ╔══════════════════════════╗
        ║     All Users Viewer     ║
        ╚══════════════════════════╝
        """.trimIndent()

    private suspend fun fetchUsers(ui: UiDisplayer): List<User> {
        ui.displayMessage("🔹 Fetching all users...")
        return getAllUsersUseCase.getAllUsers()
    }

    private fun displayUsers(ui: UiDisplayer, users: List<User>) {
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
    }
}