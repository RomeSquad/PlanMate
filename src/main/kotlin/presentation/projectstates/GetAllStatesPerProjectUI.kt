package org.example.presentation.projectstates

import org.example.logic.entity.Project
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.state.GetAllProjectStatesUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class GetAllStatesPerProjectUI(
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getAllProjectStatesUseCase: GetAllProjectStatesUseCase
) : MenuAction {

    override val description: String = buildStatesPerProjectDescription()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runCatching {
            ui.displayMessage(description)
            val projects = fetchProjects()
            val selectedProject = selectProject(ui, inputReader, projects)
            displayProjectStates(ui, selectedProject)

            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }.onFailure { exception ->
            handleError(ui, exception)
        }
    }

    private fun buildStatesPerProjectDescription(): String = """
        ╔══════════════════════════╗
        ║ States per Project Menu  ║
        ╚══════════════════════════╝
    """.trimIndent()

    private suspend fun fetchProjects(): List<Project> {
        val projects = getAllProjectsUseCase.getAllProjects()
        if (projects.isEmpty()) {
            throw IllegalStateException("No projects available to view states!")
        }
        return projects
    }

    private fun selectProject(ui: UiDisplayer, inputReader: InputReader, projects: List<Project>): Project {
        ui.displayMessage("👥 Available Projects:")
        projects.forEachIndexed { index, project ->
            ui.displayMessage("📌 ${index + 1}. ${project.name} (ID: ${project.projectId})")
        }

        ui.displayMessage("🔹 Select a project to view its states (1-${projects.size}):")
        val selectedIndex = inputReader.readString("Choice: ").trim().toIntOrNull()
        if (selectedIndex == null || selectedIndex < 1 || selectedIndex > projects.size) {
            throw IllegalArgumentException("Invalid selection. Please select a valid project number.")
        }
        return projects[selectedIndex - 1]
    }

    private suspend fun displayProjectStates(ui: UiDisplayer, project: Project) {
        ui.displayMessage("🔹 Fetching states for project '${project.name}'...")
        val states = getAllProjectStatesUseCase.execute(project.state)
        if (states.isEmpty()) {
            ui.displayMessage("⚠️ No states found for project '${project.name}'.")
        } else {
            ui.displayMessage("📌 States in Project: ${project.name}")
            states.forEachIndexed { index, state ->
                ui.displayMessage("✅ ${index + 1}. ${state.stateName} (ID: ${state.projectId})")
            }
        }
    }

    private fun handleError(ui: UiDisplayer, exception: Throwable) {
        val message = when (exception) {
            is IllegalArgumentException -> "❌ Error: ${exception.message}"
            is IllegalStateException -> "❌ Error: ${exception.message}"
            else -> "❌ An unexpected error occurred: ${exception.message ?: "Failed to retrieve project states"}"
        }
        ui.displayMessage(message)
    }
}