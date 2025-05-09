package org.example.presentation.user.admin


import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.usecase.auth.*
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import presentation.io.InputReader

class EditUserUI(
    private val editUserUseCase: EditUserUseCase,
    private val getUserByUsernameUseCase: GetUserByUsernameUseCase,
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
            ui.displayMessage("🔹 Enter Username of the user to edit:")
            val oldUsername = inputReader.readString("Username: ").trim()
            if (oldUsername.isBlank()) {
                throw IllegalArgumentException("Username must not be blank")
            }

            val oldUser = getUserByUsernameUseCase.getUserByUsername(oldUsername)
                ?: throw IllegalArgumentException("No user found with username '$oldUsername'")

            ui.displayMessage("🔹 Enter New Username (leave empty to keep '${oldUser.username}'):")
            val newUsername = inputReader.readString("New Username: ").trim().ifBlank { oldUser.username }

            ui.displayMessage("🔹 Enter New Password (leave empty to keep current):")
            val newPassword = inputReader.readString("New Password: ").trim().ifBlank { oldUser.password }

            ui.displayMessage("🔹 Select New User Role (current: ${oldUser.userRole}):")
            ui.displayMessage("1. ADMIN\n2. USER")
            val roleChoice = inputReader.readString("Role (1-2, leave empty to keep current): ").trim()
            val newRole = when {
                roleChoice.isBlank() -> oldUser.userRole
                roleChoice.toIntOrNull() == 1 -> UserRole.ADMIN
                roleChoice.toIntOrNull() == 2 -> UserRole.ADMIN
                else -> throw IllegalArgumentException("Invalid role selection. Choose 1, 2, or leave empty.")
            }

            val newUser = User(
                userId = oldUser.userId, // Keep same ID
                username = newUsername,
                password = newPassword,
                userRole = newRole
            )

            ui.displayMessage("⚠️ Update user '${oldUser.username}' to '$newUsername' with role '$newRole'? [y/n]")
            val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
            if (confirmation != "y") {
                ui.displayMessage("🛑 User edit canceled.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }

            editUserUseCase.editUser(newUser, oldUser)
            ui.displayMessage("✅ User '${oldUser.username}' updated successfully to '$newUsername'!")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: EntityNotChangedException) {
            ui.displayMessage("❌ Error: No changes made to the user${e.message}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: EmptyNameException) {
            ui.displayMessage("❌ Error: New username cannot be empty${e.message}")
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