package org.example.presentation.user.admin

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
    override val description: String = """
        ╔══════════════════════════╗
        ║     Delete a User        ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Fetching all users...")
            val users = getAllUsersUseCase.getAllUsers()
            if (users.isEmpty()) {
                ui.displayMessage("❌ No users available to delete!")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("👥 Available Users:")
            users.forEachIndexed { index, user ->
                ui.displayMessage("📌 ${index + 1}. ${user.username} (ID: ${user.userId})")
            }
            ui.displayMessage("🔹 Select a user to delete (1-${users.size}):")
            val selectedIndex = inputReader.readString("Choice: ").trim().toIntOrNull()
            if (selectedIndex == null || selectedIndex < 1 || selectedIndex > users.size) {
                ui.displayMessage("❌ Invalid selection. Please select a valid user number.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            val selectedUser = users[selectedIndex - 1]
            ui.displayMessage("🔹 You selected: ${selectedUser.username} (ID: ${selectedUser.userId})")
            ui.displayMessage("🔹 Are you sure you want to delete this user? This action cannot be undone.")
            ui.displayMessage("⚠️ Type 'YES' to confirm deletion:")
            val confirmation = inputReader.readString("Confirm: ").trim()
            if (confirmation != "YES") {
                ui.displayMessage("🛑 User deletion canceled.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("🔹 Deleting user '${selectedUser.username}'...")
            val deleted = deleteUserUseCase.deleteUser(selectedUser.username)
            if (deleted) {
                ui.displayMessage("✅ User '${selectedUser.username}' deleted successfully!")
            } else {
                ui.displayMessage("❌ Failed to delete user '${selectedUser.username}'.")
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