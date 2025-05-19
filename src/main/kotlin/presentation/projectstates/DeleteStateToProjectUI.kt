package org.example.presentation.projectstates

import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.state.DeleteProjectStatesUseCase
import org.example.logic.usecase.state.GetAllProjectStatesUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class DeleteStateToProjectUI(
    private val deleteProjectStatesUseCase: DeleteProjectStatesUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getAllProjectStatesUseCase: GetAllProjectStatesUseCase
) : MenuAction {

    override val description: String = buildDeleteStateDescription()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runCatching {
            ui.displayMessage(description)
            val projects = fetchProjects()
            val selectedProject = selectProject(ui, inputReader, projects)
            val states = fetchProjectStates(selectedProject)
            val selectedState = selectState(ui, inputReader, states)
            if (confirmStateDeletion(ui, inputReader, selectedState, selectedProject)) {
                deleteState(ui, selectedState)
                ui.displayMessage("✅ State '${selectedState.stateName}' deleted successfully from project '${selectedProject.name}'! 🎉")
            } else {
                ui.displayMessage("🛑 State deletion canceled.")
            }

            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }.onFailure { exception ->
            handleError(ui, exception)
        }
    }

    private fun buildDeleteStateDescription(): String = """
        ╔══════════════════════════╗
        ║ Delete State from Project║
        ╚══════════════════════════╝
    """.trimIndent()

    private suspend fun fetchProjects(): List<Project> {
        val projects = getAllProjectsUseCase.getAllProjects()
        if (projects.isEmpty()) {
            throw IllegalStateException("No projects available to delete states from!")
        }
        return projects
    }

    private fun selectProject(ui: UiDisplayer, inputReader: InputReader, projects: List<Project>): Project {
        ui.displayMessage("👥 Available Projects:")
        projects.forEachIndexed { index, project ->
            ui.displayMessage("📌 ${index + 1}. ${project.name} (ID: ${project.projectId})")
        }

        ui.displayMessage("🔹 Select a project to delete a state from (1-${projects.size}):")
        val selectedIndex = inputReader.readString("Choice: ").trim().toIntOrNull()
        if (selectedIndex == null || selectedIndex < 1 || selectedIndex > projects.size) {
            throw IllegalArgumentException("Invalid selection. Please select a valid project number.")
        }
        return projects[selectedIndex - 1]
    }

    private suspend fun fetchProjectStates(project: Project): List<ProjectState> {
        val states = getAllProjectStatesUseCase.execute(project.state)
        if (states.isEmpty()) {
            throw IllegalStateException("No states available to delete for project '${project.name}'!")
        }
        return states
    }

    private fun selectState(ui: UiDisplayer, inputReader: InputReader, states: List<ProjectState>): ProjectState {
        ui.displayMessage("🔹 Available States:")
        states.forEachIndexed { index, state ->
            ui.displayMessage("📌 ${index + 1}. ${state.stateName} (ID: ${state.projectId})")
        }

        ui.displayMessage("🔹 Select a state to delete (1-${states.size}):")
        val selectedIndex = inputReader.readString("Choice: ").trim().toIntOrNull()
        if (selectedIndex == null || selectedIndex < 1 || selectedIndex > states.size) {
            throw IllegalArgumentException("Invalid selection. Please select a valid state number.")
        }
        return states[selectedIndex - 1]
    }

    private fun confirmStateDeletion(
        ui: UiDisplayer,
        inputReader: InputReader,
        state: ProjectState,
        project: Project
    ): Boolean {
        ui.displayMessage("⚠️ Delete state '${state.stateName}' from project '${project.name}' (ID: ${project.projectId})? [y/n]: ")
        val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
        return confirmation == "y" || confirmation == "yes"
    }

    private suspend fun deleteState(ui: UiDisplayer, state: ProjectState) {
        ui.displayMessage("🔹 Deleting state '${state.stateName}'...")
        val deleted = deleteProjectStatesUseCase.execute(state.projectId)
        if (!deleted) {
            throw IllegalStateException("Failed to delete state '${state.stateName}'.")
        }
    }

    private fun handleError(ui: UiDisplayer, exception: Throwable) {
        val message = when (exception) {
            is IllegalArgumentException -> "❌ Error: ${exception.message}"
            is IllegalStateException -> "❌ Error: ${exception.message}"
            else -> "❌ An unexpected error occurred: ${exception.message ?: "Failed to delete state"}"
        }
        ui.displayMessage(message)
    }
}