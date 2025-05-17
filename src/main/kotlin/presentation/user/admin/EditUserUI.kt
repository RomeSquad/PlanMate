package org.example.presentation.user.admin


import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.EmptyPasswordException
import org.example.logic.exception.EntityNotChangedException
import org.example.logic.request.EditUserRequest
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
    override val description: String = buildDescription()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            val users = fetchUsers(ui)
            if (users.isEmpty()) {
                ui.displayMessage("❌ No users available to edit!")
                return
            }
            val selectedUser = selectUser(ui, inputReader, users)
            if (!confirmUserSelection(ui, inputReader, selectedUser)) {
                ui.displayMessage("🛑 User edit canceled.")
                return
            }
            val userInput = collectUserInput(ui, inputReader, selectedUser)
            if (!confirmUserUpdate(ui, inputReader, selectedUser, userInput)) {
                ui.displayMessage("🛑 User edit canceled.")
                return
            }
            updateUser(ui, selectedUser.username, userInput)
            ui.displayMessage("✅ User '${selectedUser.username}' updated successfully!")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Invalid input: ${e.message ?: "Invalid data provided"}")
        } catch (e: EntityNotChangedException) {
            ui.displayMessage("❌ No changes made to the user: ${e.message ?: "No updates provided"}")
        } catch (e: EmptyPasswordException) {
            ui.displayMessage("❌ New password cannot be empty: ${e.message ?: "Invalid password"}")
        } catch (e: Exception) {
            ui.displayMessage("❌ Error: ${e.message ?: "Failed to update user"}")
        }
    }

    private fun buildDescription(): String = """
        ╔══════════════════════════╗
        ║      Edit a User         ║
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
        ui.displayMessage("🔹 Select a user to edit (1-$max):")
        val choice = inputReader.readString("Choice: ").trim().toIntOrNull()
        if (choice == null || choice < 1 || choice > max) {
            throw IllegalArgumentException("Please select a number between 1 and $max")
            }
        return choice
    }

    private fun confirmUserSelection(ui: UiDisplayer, inputReader: InputReader, user: User): Boolean {
        ui.displayMessage("⚠️ Edit user '${user.username}' (ID: ${user.userId})? [y/n]")
        val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
        return confirmation == "y"
    }

    private fun collectUserInput(ui: UiDisplayer, inputReader: InputReader, user: User): UserInput {
        ui.displayMessage("🔹 Editing user '${user.username}'...")
        val newPassword = readOptionalInput(ui, inputReader, "New Password", "leave empty to keep current")
        val newRole = selectUserRole(ui, inputReader, user.userRole)
        return UserInput(newPassword, newRole)
    }

    private fun readOptionalInput(
        ui: UiDisplayer,
        inputReader: InputReader,
        field: String,
        hint: String
    ): String {
        ui.displayMessage("🔹 Enter $field ($hint):")
        return inputReader.readString("$field: ").trim()
    }

    private fun selectUserRole(ui: UiDisplayer, inputReader: InputReader, currentRole: UserRole): UserRole {
        ui.displayMessage("🔹 Select New User Role (current: $currentRole):")
            ui.displayMessage("1. ADMIN\n2. MATE")
        val choice = inputReader.readString("Role (1-2, leave empty to keep current): ").trim()
        return when {
            choice.isBlank() -> currentRole
            choice.toIntOrNull() == 1 -> UserRole.ADMIN
            choice.toIntOrNull() == 2 -> UserRole.MATE
            else -> throw IllegalArgumentException("Invalid role selection. Choose 1, 2, or leave empty")
        }
    }

    private fun confirmUserUpdate(
        ui: UiDisplayer,
        inputReader: InputReader,
        user: User,
        input: UserInput
    ): Boolean {
        ui.displayMessage("⚠️ Update user '${user.username}' with role '${input.userRole}'? [y/n]")
        val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
        return confirmation == "y"
    }

    private suspend fun updateUser(ui: UiDisplayer, username: String, input: UserInput) {
        ui.displayMessage("🔹 Updating user '$username'...")
            editUserUseCase.editUser(
                request = EditUserRequest(
                    username = username,
                    password = input.password,
                    userRole = input.userRole
                )
            )
    }

    private data class UserInput(
        val password: String,
        val userRole: UserRole
    )
}