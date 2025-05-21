package org.example.presentation.user.admin


import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.request.EditUserRequest
import org.example.logic.usecase.auth.EditUserUseCase
import org.example.logic.usecase.auth.GetAllUsersUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class EditUserUI(
    private val editUserUseCase: EditUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase
) : BaseMenuAction() {

    override val title: String = "Edit a User"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val users = fetchEntities(ui, { getAllUsersUseCase.getAllUsers() }, "users")
            val selectedUser = selectEntity(
                ui, inputReader, users, "Users",
                prompt = "🔹 Select a user to edit (1-${users.size}): ",
                format = { user, index -> "👤 $index. ${user.username} | Role: ${user.userRole} | ID: ${user.userId}" }
            ) ?: run {
                ui.displayMessage("❌ No users available to edit!")
                return@executeWithErrorHandling
            }
            ui.displayMessage("🔹 Editing user '${selectedUser.username}'...")
            val userInput = collectUserInput(ui, inputReader, selectedUser)
            if (userInput.password.isEmpty() && userInput.userRole == selectedUser.userRole) {
                ui.displayMessage("🛑 No changes made to user.")
                return@executeWithErrorHandling
            }
            if (!confirmAction(
                    ui, inputReader,
                    "⚠️ Update user '${selectedUser.username}' with role '${userInput.userRole}'? [y/n]: "
                )
            ) {
                ui.displayMessage("🛑 User edit canceled.")
                return@executeWithErrorHandling
            }
            ui.displayMessage("🔹 Updating user '${selectedUser.username}'...")
            editUserUseCase.editUser(
                request = EditUserRequest(
                    username = selectedUser.username,
                    password = userInput.password,
                    userRole = userInput.userRole
                )
            )
            ui.displayMessage("✅ User '${selectedUser.username}' updated successfully!")
        }
    }

    private data class UserInput(val password: String, val userRole: UserRole)

    private fun collectUserInput(ui: UiDisplayer, inputReader: InputReader, user: User): UserInput {
        val newPassword = readValidatedInput(
            ui, inputReader, "🔹 Enter New Password:", "New Password", "Invalid password",
            { it.takeIf { it.isNotBlank() } ?: "" }, hint = "leave empty to keep current"
        )
        val newRole = selectUserRole(ui, inputReader, user.userRole)
        return UserInput(newPassword, newRole)
    }

    override fun selectUserRole(
        ui: UiDisplayer,
        inputReader: InputReader,
        currentRole: UserRole?,
        prompt: String
    ): UserRole {
        val roleOptions = UserRole.entries.joinToString(", ") { it.name }
        return readValidatedInput(
            ui, inputReader, "🔹 Enter User Role ($roleOptions) [default: ${currentRole ?: "none"}]:", "User Role",
            "Invalid role. Choose from $roleOptions",
            { UserRole.entries.find { role -> role.name.equals(it, ignoreCase = true) } ?: currentRole },
            hint = "leave empty to keep ${currentRole ?: "none"}"
        )
    }
}