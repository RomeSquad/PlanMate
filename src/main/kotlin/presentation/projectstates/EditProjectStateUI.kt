package org.example.presentation.projectstates


import logic.usecase.project.EditProjectUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class EditProjectStateUI(
    private val editProjectUseCase: EditProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : BaseMenuAction() {

    override val title: String = "Edit Project State"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val projects = fetchEntities(ui, { getAllProjectsUseCase.getAllProjects() }, "projects")
            val selectedProject = selectEntity(
                ui, inputReader, projects, "Projects",
                format = { project, index -> "📌 $index. ${project.name} | ID: ${project.projectId}" }
            ) ?: run {
                ui.displayMessage("❌ No projects available to edit states!")
                return@executeWithErrorHandling
            }
            ui.displayMessage("🔹 Current State: ${selectedProject.state.stateName}")
            val newStateName = readValidatedInput(
                ui, inputReader, "🔹 Enter new state name:", "State Name", "State name must not be blank",
                { it.takeIf { it.isNotBlank() } ?: selectedProject.state.stateName },
                hint = "leave empty to keep '${selectedProject.state.stateName}'"
            )
            if (newStateName == selectedProject.state.stateName) {
                ui.displayMessage("🛑 No changes made to state.")
                return@executeWithErrorHandling
            }
            if (!confirmAction(
                    ui, inputReader,
                    "⚠️ Update state of project '${selectedProject.name}' (ID: ${selectedProject.projectId}) to '$newStateName'? [y/n]: "
                )
            ) {
                ui.displayMessage("🛑 State update canceled.")
                return@executeWithErrorHandling
            }
            ui.displayMessage("🔹 Updating state for project '${selectedProject.name}'...")
            val updatedProject = selectedProject.copy(state = selectedProject.state.copy(stateName = newStateName))
            editProjectUseCase.execute(updatedProject)
            ui.displayMessage("✅ Project '${selectedProject.name}' state updated successfully to '$newStateName'! 🎉")
        }
    }
}