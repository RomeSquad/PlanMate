package org.example.presentation.user.admin

import org.example.logic.entity.auth.UserRole
import org.example.logic.usecase.auth.InsertUserUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class CreateUserUi(
    private val insertUserUseCase: InsertUserUseCase
) : BaseMenuAction() {

    override val title: String = "Create a New User"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val userInput = collectUserInput(ui, inputReader)
            if (!confirmAction(
                    ui, inputReader,
                    "⚠️ Create user '${userInput.username}' with role '${userInput.userRole}'? [y/n]: "
                )
            ) {
                ui.displayMessage("🛑 User creation canceled.")
                return@executeWithErrorHandling
            }
            val user = insertUserUseCase.insertUser(userInput.username, userInput.password, userInput.userRole)
            ui.displayMessage("✅ User '${user.username}' created successfully with ID '${user.userId}'!")
        }
    }

    private data class UserInput(val username: String, val password: String, val userRole: UserRole)

    private fun collectUserInput(ui: UiDisplayer, inputReader: InputReader): UserInput {
        val username = readValidatedInput(
            ui, inputReader, "🔹 Enter Username:", "Username", "Username must not be blank",
            ::nonBlankValidator
        )
        val password = readValidatedInput(
            ui, inputReader, "🔹 Enter Password:", "Password", "Password must not be blank",
            ::nonBlankValidator
        )
        val userRole = selectUserRole(ui, inputReader)
        return UserInput(username, password, userRole)
    }

    override fun selectUserRole(
        ui: UiDisplayer,
        inputReader: InputReader,
        currentRole: UserRole?,
        prompt: String
    ): UserRole {
        val roleOptions = UserRole.entries.joinToString(", ") { it.name }
        return readValidatedInput(
            ui, inputReader, "🔹 Enter User Role ($roleOptions):", "User Role", "Invalid role. Choose from $roleOptions",
            { UserRole.entries.find { role -> role.name.equals(it, ignoreCase = true) } }
        )
    }
}