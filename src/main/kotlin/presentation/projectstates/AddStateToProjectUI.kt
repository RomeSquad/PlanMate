package org.example.presentation.projectstates

import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.state.AddProjectStatesUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction


class AddStateToProjectUI(
    private val addProjectStatesUseCase: AddProjectStatesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : BaseMenuAction() {

    override val title: String = "Add State to Project"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val currentUser = getCurrentUser(getCurrentUserUseCase)
            if (currentUser == null) {
                ui.displayMessage("❌ User not logged in.")
                return@executeWithErrorHandling
            }
            ui.displayMessage("🔹 Current User: ${currentUser.username} | ID: ${currentUser.userId}")
            val projects = fetchEntities(ui, { getAllProjectsUseCase.getAllProjects() }, "projects")
            val selectedProject = selectEntity(
                ui, inputReader, projects, "Projects",
                format = { project, index -> "📌 $index. ${project.name} | ID: ${project.projectId}" }
            ) ?: run {
                ui.displayMessage("❌ No projects available to add states to!")
                return@executeWithErrorHandling
            }
            val stateName = readValidatedInput(
                ui, inputReader, "🔹 Enter state name:", "State Name", "State name must not be blank",
                ::nonBlankValidator
            )
            if (!confirmAction(
                    ui, inputReader,
                    "⚠️ Add state '$stateName' to project '${selectedProject.name}' (ID: ${selectedProject.projectId})? [y/n]: "
                )
            ) {
                ui.displayMessage("🛑 State addition canceled.")
                return@executeWithErrorHandling
            }
            ui.displayMessage("🔹 Adding state to project '${selectedProject.name}'...")
            addProjectStatesUseCase.execute(projectId = selectedProject.projectId, stateName = stateName)
            ui.displayMessage("✅ State '$stateName' added successfully to project '${selectedProject.name}'! 🎉")
        }
    }
}