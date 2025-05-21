package org.example.presentation.user.admin

import org.example.logic.usecase.auth.DeleteUserUseCase
import org.example.logic.usecase.auth.GetAllUsersUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class DeleteUserUi(
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase
) : BaseMenuAction() {

    override val title: String = "Delete a User"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val users = fetchEntities(ui, { getAllUsersUseCase.getAllUsers() }, "users")
            val selectedUser = selectEntity(
                ui, inputReader, users, "Users",
                prompt = "🔹 Select a user to delete (1-${users.size}): ",
                format = { user, index -> "👤 $index. ${user.username} | Role: ${user.userRole} | ID: ${user.userId}" }
            ) ?: run {
                ui.displayMessage("❌ No users available to delete!")
                return@executeWithErrorHandling
            }
            if (!confirmAction(
                    ui, inputReader,
                    "⚠️ Delete user '${selectedUser.username}' (ID: ${selectedUser.userId})? [y/n]: "
                )
            ) {
                ui.displayMessage("🛑 User deletion canceled.")
                return@executeWithErrorHandling
            }
            ui.displayMessage("🔹 Deleting user '${selectedUser.username}'...")
            val deleted = deleteUserUseCase.deleteUser(selectedUser.username)
            if (deleted) {
                ui.displayMessage("✅ User '${selectedUser.username}' deleted successfully!")
            } else {
                throw IllegalStateException("Failed to delete user '${selectedUser.username}'")
            }
        }
    }
}