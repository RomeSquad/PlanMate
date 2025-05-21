package org.example.presentation.project

import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.presentation.history.ShowHistoryManagementUI
import org.example.presentation.projectstates.ProjectStateManagementUI
import org.example.presentation.task.TaskManagementUI
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction


class ProjectManagementUI(
    private val createProjectUi: CreateProjectUI,
    private val deleteProjectUi: DeleteProjectUI,
    private val editProjectUi: EditProjectUI,
    private val listProjectUi: ListProjectUI,
    private val taskManagementUi: TaskManagementUI,
    private val projectStateManagementUI: ProjectStateManagementUI,
    private val changeHistoryManagementUI: ShowHistoryManagementUI,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : BaseMenuAction() {

    override val title: String = "Project Management Menu"

    private val menuOptions = listOf(
        MenuOption(1, "Create New Project", menuAction = createProjectUi),
        MenuOption(2, "Delete Project", menuAction = deleteProjectUi),
        MenuOption(3, "Edit Project", menuAction = editProjectUi),
        MenuOption(4, "List All Projects", menuAction = listProjectUi),
        MenuOption(5, "View Project Logs", menuAction = changeHistoryManagementUI),
        MenuOption(6, "Manage Tasks", menuAction = taskManagementUi),
        MenuOption(7, "Manage Project States", menuAction = projectStateManagementUI),
        MenuOption(8, "Back to Main Menu")
    )

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            if (getCurrentUser(getCurrentUserUseCase) == null) {
                ui.displayMessage("❌ User not logged in.")
                return@executeWithErrorHandling
            }
            runMenuLoop(ui, inputReader, menuOptions) { it.number == 8 }
        }
    }
}