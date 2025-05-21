package org.example.presentation.projectstates

import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.state.GetAllProjectStatesUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class GetAllStatesPerProjectUI(
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getAllProjectStatesUseCase: GetAllProjectStatesUseCase
) : BaseMenuAction() {

    override val title: String = "States per Project Menu"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val projects = fetchEntities(ui, { getAllProjectsUseCase.getAllProjects() }, "projects")
            val selectedProject = selectEntity(
                ui, inputReader, projects, "Projects",
                format = { project, index -> "📌 $index. ${project.name} | ID: ${project.projectId}" }
            ) ?: run {
                ui.displayMessage("❌ No projects available to view states!")
                return@executeWithErrorHandling
            }
            val states = fetchEntities(ui, { getAllProjectStatesUseCase.execute(selectedProject.state) }, "states")
            displayEntities(
                ui, states, "States in Project '${selectedProject.name}'",
                { state, index -> "✅ $index. ${state.stateName} | ID: ${state.projectId}" }
            )
        }
    }
}