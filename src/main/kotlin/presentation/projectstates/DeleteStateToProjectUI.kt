package org.example.presentation.projectstates

import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.state.DeleteProjectStatesUseCase
import org.example.logic.usecase.state.GetAllProjectStatesUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class DeleteStateToProjectUI(
    private val deleteProjectStatesUseCase: DeleteProjectStatesUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getAllProjectStatesUseCase: GetAllProjectStatesUseCase
) : BaseMenuAction() {

    override val title: String = "Delete State from Project"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val projects = fetchEntities(ui, { getAllProjectsUseCase.getAllProjects() }, "projects")
            val selectedProject = selectEntity(
                ui, inputReader, projects, "Projects",
                format = { project, index -> "📌 $index. ${project.name} | ID: ${project.projectId}" }
            ) ?: run {
                ui.displayMessage("❌ No projects available to delete states from!")
                return@executeWithErrorHandling
            }
            val states = fetchEntities(ui, { getAllProjectStatesUseCase.execute(selectedProject.state) }, "states")
            val selectedState = selectEntity(
                ui, inputReader, states, "States",
                prompt = "🔹 Select a state to delete (1-${states.size}): ",
                format = { state, index -> "✅ $index. ${state.stateName} | ID: ${state.projectId}" }
            ) ?: run {
                ui.displayMessage("❌ No states available to delete for project '${selectedProject.name}'!")
                return@executeWithErrorHandling
            }
            if (!confirmAction(
                    ui, inputReader,
                    "⚠️ Delete state '${selectedState.stateName}' from project '${selectedProject.name}' (ID: ${selectedProject.projectId})? [y/n]: "
                )
            ) {
                ui.displayMessage("🛑 State deletion canceled.")
                return@executeWithErrorHandling
            }
            ui.displayMessage("🔹 Deleting state '${selectedState.stateName}'...")
            val deleted = deleteProjectStatesUseCase.execute(selectedState.projectId)
            if (!deleted) {
                throw IllegalStateException("Failed to delete state '${selectedState.stateName}'.")
            }
            ui.displayMessage("✅ State '${selectedState.stateName}' deleted successfully from project '${selectedProject.name}'! 🎉")
        }
    }
}