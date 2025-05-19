package org.example.presentation.projectstates


import logic.usecase.project.EditProjectUseCase
import org.example.logic.entity.Project
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class EditProjectStateUI(
    private val editProjectUseCase: EditProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : MenuAction {

    override val description: String = buildEditStateDescription()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runCatching {
            ui.displayMessage(description)
            val projects = fetchProjects()
            val selectedProject = selectProject(ui, inputReader, projects)
            val newStateName = collectNewStateName(ui, inputReader, selectedProject)
            if (confirmStateUpdate(ui, inputReader, selectedProject, newStateName)) {
                updateProjectState(ui, selectedProject, newStateName)
                ui.displayMessage("✅ Project '${selectedProject.name}' state updated successfully to '$newStateName'! 🎉")
            } else {
                ui.displayMessage("🛑 State update canceled.")
            }

            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }.onFailure { exception ->
            handleError(ui, exception)
        }
    }

    private fun buildEditStateDescription(): String = """
        ╔══════════════════════════╗
        ║   Edit Project State     ║
        ╚══════════════════════════╝
    """.trimIndent()

    private suspend fun fetchProjects(): List<Project> {
        val projects = getAllProjectsUseCase.getAllProjects()
        if (projects.isEmpty()) {
            throw IllegalStateException("No projects available to edit states!")
        }
        return projects
    }

    private fun selectProject(ui: UiDisplayer, inputReader: InputReader, projects: List<Project>): Project {
        ui.displayMessage("👥 Available Projects:")
        projects.forEachIndexed { index, project ->
            ui.displayMessage("📌 ${index + 1}. ${project.name} (ID: ${project.projectId})")
        }

        ui.displayMessage("🔹 Select a project to edit its state (1-${projects.size}):")
        val selectedIndex = inputReader.readString("Choice: ").trim().toIntOrNull()
        if (selectedIndex == null || selectedIndex < 1 || selectedIndex > projects.size) {
            throw IllegalArgumentException("Invalid selection. Please select a valid project number.")
        }
        return projects[selectedIndex - 1]
    }

    private fun collectNewStateName(ui: UiDisplayer, inputReader: InputReader, project: Project): String {
        ui.displayMessage("🔹 Current State: ${project.state.stateName}")
        return readNonBlankInput(
            ui,
            inputReader,
            "🔹 Enter new state name:",
            "State Name",
            "State name must not be blank"
        )
    }

    private fun confirmStateUpdate(
        ui: UiDisplayer,
        inputReader: InputReader,
        project: Project,
        newStateName: String
    ): Boolean {
        ui.displayMessage("⚠️ Update state of project '${project.name}' (ID: ${project.projectId}) to '$newStateName'? [y/n]: ")
        val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
        return confirmation == "y" || confirmation == "yes"
    }

    private suspend fun updateProjectState(ui: UiDisplayer, project: Project, newStateName: String) {
        ui.displayMessage("🔹 Updating state for project '${project.name}'...")
        val updatedProject = project.copy(state = project.state.copy(stateName = newStateName))
        editProjectUseCase.execute(updatedProject)
    }

    private fun readNonBlankInput(
        ui: UiDisplayer,
        inputReader: InputReader,
        prompt: String,
        label: String,
        errorMessage: String
    ): String {
        ui.displayMessage(prompt)
        val input = inputReader.readString("$label: ").trim()
        if (input.isBlank()) throw IllegalArgumentException(errorMessage)
        return input
    }

    private fun handleError(ui: UiDisplayer, exception: Throwable) {
        val message = when (exception) {
            is IllegalArgumentException -> "❌ Error: ${exception.message}"
            is IllegalStateException -> "❌ Error: ${exception.message}"
            else -> "❌ An unexpected error occurred: ${exception.message ?: "Failed to update project state"}"
        }
        ui.displayMessage(message)
    }
}