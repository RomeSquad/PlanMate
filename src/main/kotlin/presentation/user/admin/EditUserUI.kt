package org.example.presentation.user.admin


import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.EmptyPasswordException
import org.example.logic.exception.EntityNotChangedException
import org.example.logic.usecase.auth.EditUserUseCase
import org.example.logic.usecase.auth.GetAllUsersUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class EditUserUI(
    private val editUserUseCase: EditUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║      Edit a User         ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Fetching all users...")
            val users = getAllUsersUseCase.getAllUsers()
            if (users.isEmpty()) {
                ui.displayMessage("❌ No users available to edit!")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("👥 Available Users:")
            users.forEachIndexed { index, user ->
                ui.displayMessage("📌 ${index + 1}. ${user.username} (ID: ${user.userId})")
            }
            ui.displayMessage("🔹 Select a user to edit (1-${users.size}):")
            val selectedIndex = inputReader.readString("Choice: ").trim().toIntOrNull()
            if (selectedIndex == null || selectedIndex < 1 || selectedIndex > users.size) {
                ui.displayMessage("❌ Invalid selection. Please select a valid user number.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            val selectedUser = users[selectedIndex - 1]
            ui.displayMessage("🔹 You selected: ${selectedUser.username} (ID: ${selectedUser.userId})")
            ui.displayMessage("🔹 Are you sure you want to edit this user?")
            ui.displayMessage("⚠️ Type 'YES' to confirm editing:")
            val confirmation = inputReader.readString("Confirm: ").trim()
            if (confirmation != "YES") {
                ui.displayMessage("🛑 User edit canceled.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("🔹 Editing user '${selectedUser.username}'...")
            ui.displayMessage("🔹 Enter New Password (leave empty to keep current):")
            val newPassword = inputReader.readString("New Password: ").trim().ifBlank { selectedUser.password }
            ui.displayMessage("🔹 Select New User Role (current: ${selectedUser.userRole}):")
            ui.displayMessage("1. ADMIN\n2. MATE")
            val roleChoice = inputReader.readString("Role (1-2, leave empty to keep current): ").trim()
            val newRole = when {
                roleChoice.isBlank() -> selectedUser.userRole
                roleChoice.toIntOrNull() == 1 -> UserRole.ADMIN
                roleChoice.toIntOrNull() == 2 -> UserRole.MATE
                else -> throw IllegalArgumentException("Invalid role selection. Choose 1, 2, or leave empty.")
            }
            val newUser = selectedUser.copy(
                password = newPassword,
                userRole = newRole
            )
            ui.displayMessage("⚠️ Update user '${selectedUser.username}'' with role '$newRole'? [y/n]")
            val editConfirmation = inputReader.readString("Confirm: ").trim().lowercase()
            if (editConfirmation != "y") {
                ui.displayMessage("🛑 User edit canceled.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("🔹 Updating user '${selectedUser.username}'...")
            editUserUseCase.editUser(
                newUser = newUser,
                oldUser = selectedUser
            )
            ui.displayMessage("✅ User '${selectedUser.username}' updated successfully!")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: EntityNotChangedException) {
            ui.displayMessage("❌ Error: No changes made to the user${e.message}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: EmptyPasswordException) {
            ui.displayMessage("❌ Error: New password cannot be empty${e.message}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to update user"}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}