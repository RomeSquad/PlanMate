package org.example.presentation.project

import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.presentation.history.ShowHistoryManagementUI
import org.example.presentation.projectstates.ProjectStateManagementUI
import org.example.presentation.task.TaskManagementUI
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class ProjectManagementUI(
    private val createProjectUi: CreateProjectUi,
    private val deleteProjectUi: DeleteProjectUi,
    private val editProjectUi: EditProjectUi,
    private val listProjectUi: ListProjectUi,
    private val taskManagementUi: TaskManagementUI,
    private val projectStateManagementUI: ProjectStateManagementUI,
    private val changeHistoryManagementUI: ShowHistoryManagementUI,
    private val getCurrentUser: GetCurrentUserUseCase
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║ Project Management Menu  ║
        ╚══════════════════════════╝
    """.trimIndent()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        val user = getCurrentUser.getCurrentUser()

        if (user == null) {
            ui.displayMessage("❌ No authenticated user found! Please log in first.")
            return
        }

        ui.displayMessage("👤 Welcome ${user.username} (${user.userRole})!")

        while (true) {
            ui.displayMessage(description)
            ui.displayMessage(
                """
                ➕ 1. Create New Project
                🗑 2. Delete Project
                ✏️ 3. Edit Project
                📜 4. List All Projects
                📜 5. View Project Logs
                📋 6. Manage Tasks
                📋 7. Manage Project States
                ⬅️ 8. Back to Main Menu
                """.trimIndent()
            )
            ui.displayMessage("🔹 Choose an option (1-8):")
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {
                1 -> createProjectUi.execute(ui, inputReader)
                2 -> deleteProjectUi.execute(ui, inputReader)
                3 -> editProjectUi.execute(ui, inputReader)
                4 -> listProjectUi.execute(ui, inputReader)
                5 -> changeHistoryManagementUI.execute(ui, inputReader)
                6 -> taskManagementUi.execute(ui, inputReader)
                7 -> projectStateManagementUI.execute(ui, inputReader)
                8 -> {
                    ui.displayMessage("🔙 Returning to Main Menu...")
                    return
                }
                else -> ui.displayMessage("❌ Invalid option. Please select a number between 1 and 8.")
            }

            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}