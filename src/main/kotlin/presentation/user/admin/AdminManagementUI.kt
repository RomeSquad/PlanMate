package org.example.presentation.user.admin

import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.presentation.history.ShowHistoryManagementUI
import org.example.presentation.project.ProjectManagementUI
import org.example.presentation.task.TaskManagementUI
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class AdminManagementUI(
    private val projectManagementUI: ProjectManagementUI,
    private val taskManagementUI: TaskManagementUI,
    private val createUserUi: CreateUserUi,
    private val deleteUserUi: DeleteUserUi,
    private val editUserUI: EditUserUI,
    private val viewAllUserUI: ViewAllUserUI,
    private val changeHistoryManagementUI: ShowHistoryManagementUI,
    private val getCurrentUser: GetCurrentUserUseCase
) : MenuAction {

    override val description: String = """
        ╔════════════════════════╗
        ║  Admin Control Center  ║
        ╚════════════════════════╝
    """.trimIndent()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        val user = getCurrentUser.getCurrentUser()

        if (user == null) {
            ui.displayMessage("❌ No authenticated user found! Please log in first.")
            return
        }

        ui.displayMessage("👤 Welcome Admin ${user.username} (${user.userRole})!")

        while (true) {
            ui.displayMessage(description)
            ui.displayMessage(
                """
                📂 1. Manage Projects
                👥 2. Manage Users
                ✅ 3. Manage Tasks
                📜 4. View Audit Logs
                🚪 5. Logout
                """.trimIndent()
            )
            ui.displayMessage("🔹 Select an option (1-5):")
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {
                1 -> projectManagementUI.execute(ui, inputReader)
                2 -> manageUsers(ui, inputReader)
                3 -> taskManagementUI.execute(ui, inputReader)
                4 -> changeHistoryManagementUI.execute(ui, inputReader)
                5 -> {
                    ui.displayMessage("🔙 Logging out...")
                    return
                }
                else -> ui.displayMessage("❌ Invalid option. Please select a number between 1 and 5.")
            }

            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }

    private suspend fun manageUsers(ui: UiDisplayer, inputReader: InputReader) {
        while (true) {
            ui.displayMessage(
                """
                ╔══════════════════════════╗
                ║     User Management      ║
                ╚══════════════════════════╝
                """.trimIndent()
            )
            ui.displayMessage(
                """
                ➕ 1. Create User
                🗑 2. Delete User
                ✏️ 3. Edit User
                📜 4. View All Users
                ⬅️ 5. Back
                """.trimIndent()
            )
            ui.displayMessage("🔹 Select an option (1-5):")
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {
                1 -> createUserUi.execute(ui, inputReader)
                2 -> deleteUserUi.execute(ui, inputReader)
                3 -> editUserUI.execute(ui, inputReader)
                4 -> viewAllUserUI.execute(ui, inputReader)
                5 -> return
                else -> ui.displayMessage("❌ Invalid option. Please select a number between 1 and 5.")
            }

            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}