package org.example.presentation.user.admin

import org.example.logic.entity.auth.User
import org.example.logic.usecase.auth.DeleteUserUseCase
import org.example.logic.usecase.auth.GetAllUsersUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class DeleteUserUi(
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase
) : MenuAction {
    override val description: String = buildDescription()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            val users = fetchUsers(ui)
            if (users.isEmpty()) {
                ui.displayMessage("❌ No users available to delete!")
                return
            }
            val selectedUser = selectUser(ui, inputReader, users)
            if (!confirmUserDeletion(ui, inputReader, selectedUser)) {
                ui.displayMessage("🛑 User deletion canceled.")
                return
            }
            deleteUser(ui, selectedUser)
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Invalid input: ${e.message ?: "Invalid selection"}")
        } catch (e: Exception) {
            ui.displayMessage("❌ Error: ${e.message ?: "Failed to delete user"}")
        }
    }

    private fun buildDescription(): String = """
        ╔══════════════════════════╗
        ║     Delete a User        ║
        ╚══════════════════════════╝
        """.trimIndent()

    private suspend fun fetchUsers(ui: UiDisplayer): List<User> {
        ui.displayMessage("🔹 Fetching all users...")
        return getAllUsersUseCase.getAllUsers()
    }

    private fun selectUser(ui: UiDisplayer, inputReader: InputReader, users: List<User>): User {
        displayUsers(ui, users)
        val choice = readValidatedChoice(ui, inputReader, users.size)
        return users[choice - 1]
    }

    private fun displayUsers(ui: UiDisplayer, users: List<User>) {
        ui.displayMessage("👥 Available Users:")
        users.forEachIndexed { index, user ->
            ui.displayMessage("📌 ${index + 1}. ${user.username} (ID: ${user.userId})")
        }
    }

    private fun readValidatedChoice(ui: UiDisplayer, inputReader: InputReader, max: Int): Int {
        ui.displayMessage("🔹 Select a user to delete (1-$max):")
        val choice = inputReader.readString("Choice: ").trim().toIntOrNull()
        if (choice == null || choice < 1 || choice > max) {
            throw IllegalArgumentException("Please select a number between 1 and $max")
        }
        return choice
    }

    private fun confirmUserDeletion(ui: UiDisplayer, inputReader: InputReader, user: User): Boolean {
        ui.displayMessage("⚠️ Delete user '${user.username}' (ID: ${user.userId})? [y/n]")
        val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
        return confirmation == "y"
    }

    private suspend fun deleteUser(ui: UiDisplayer, user: User) {
        ui.displayMessage("🔹 Deleting user '${user.username}'...")
        val deleted = deleteUserUseCase.deleteUser(user.username)
        if (deleted) {
            ui.displayMessage("✅ User '${user.username}' deleted successfully!")
        } else {
            throw IllegalStateException("Failed to delete user '${user.username}'")
        }
    }
}