package org.example.presentation.project

import org.example.logic.usecase.project.SaveAllProjectUseCase
import org.example.presentation.history.ShowProjectHistoryUI
import org.example.presentation.projectstates.ProjectStateManagementUI
import org.example.presentation.task.TaskManagementUI
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import presentation.io.InputReader


class ProjectManagementUI(
    private val createProjectUi: CreateProjectUi,
    private val deleteProjectUi: DeleteProjectUi,
    private val editProjectUi: EditProjectUi,
    private val listProjectUi: ListProjectUi,
    private val getProjectByIdUI: GetProjectByIdUI,
    private val showProjectHistoryUI: ShowProjectHistoryUI,
    private val taskManagementUi: TaskManagementUI,
    private val projectStateManagementUI: ProjectStateManagementUI,
    private val saveAllProjectUseCase: SaveAllProjectUseCase
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║ Project Management Menu  ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    private val options = listOf(
        "➕ 1. Create New Project",
        "🗑️ 2. Delete Project",
        "✏️ 3. Edit Project",
        "📜 4. List All Projects",
        "🔍 5. Get Project by ID",
        "📜 6. Show Project History",
        "📋 7. Manage Tasks",
        "📋 8. Manage Project States",
        "⬅️ 9. Back to Main Menu"
    )


    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        while (true) {
            ui.displayMessage(description)
            ui.displayMessage(options.joinToString("\n"))
            ui.displayMessage("🔹 Choose an option (1-8):")
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {
                1 -> createProjectUi.execute(ui, inputReader)
                2 -> deleteProjectUi.execute(ui, inputReader)
                3 -> editProjectUi.execute(ui, inputReader)
                4 -> listProjectUi.execute(ui, inputReader)
                5 -> getProjectByIdUI.execute(ui, inputReader)
                6 -> showProjectHistoryUI.execute(ui, inputReader)
                7 -> taskManagementUi.execute(ui, inputReader)
                8 -> projectStateManagementUI.execute(ui, inputReader)
                9 -> {
                    saveAllProjectUseCase.saveProjects().fold(
                        onSuccess = { ui.displayMessage("✅ All projects saved successfully!") },
                        onFailure = { ui.displayMessage("❌ Failed to save projects: ${it.message}") }
                    )
                    ui.displayMessage("🔙 Returning to Main Menu...")
                    return
                }

                else -> ui.displayMessage("❌ Invalid choice. Please select a number between 1 and 8.")
            }
        }
    }
}

