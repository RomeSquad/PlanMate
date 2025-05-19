package org.example.presentation.projectstates

import org.example.logic.entity.Project
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.state.AddProjectStatesUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction


class AddStateToProjectUI(
    private val addProjectStatesUseCase: AddProjectStatesUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : MenuAction {

    override val description: String = buildAddStateDescription()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runCatching {
            ui.displayMessage(description)
            val currentUser = getCurrentUser.getCurrentUser()
                ?: throw IllegalStateException("No authenticated user found. Please log in.")

            ui.displayMessage("🔹 Current User: ${currentUser.username} (ID: ${currentUser.userId})")
            val projects = fetchProjects()
            val selectedProject = selectProject(ui, inputReader, projects)
            if (confirmStateAddition(ui, inputReader, selectedProject)) {
                addStateToProject(ui, inputReader, selectedProject)
                ui.displayMessage("✅ State added successfully to project '${selectedProject.name}'! 🎉")
            } else {
                ui.displayMessage("🛑 State addition canceled.")
            }

            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }.onFailure { exception ->
            handleError(ui, exception)
        }
    }

    private fun buildAddStateDescription(): String = """
        ╔══════════════════════════╗
        ║  Add State to Project    ║
        ╚══════════════════════════╝
    """.trimIndent()

    private suspend fun fetchProjects(): List<Project> {
        val projects = getAllProjectsUseCase.getAllProjects()
        if (projects.isEmpty()) {
            throw IllegalStateException("No projects available to add states to!")
        }
        return projects
    }

    private fun selectProject(ui: UiDisplayer, inputReader: InputReader, projects: List<Project>): Project {
        ui.displayMessage("👥 Available Projects:")
        projects.forEachIndexed { index, project ->
            ui.displayMessage("📌 ${index + 1}. ${project.name} (ID: ${project.projectId})")
        }

        ui.displayMessage("🔹 Select a project to add a state to (1-${projects.size}):")
        val selectedIndex = inputReader.readString("Choice: ").trim().toIntOrNull()
        if (selectedIndex == null || selectedIndex < 1 || selectedIndex > projects.size) {
            throw IllegalArgumentException("Invalid selection. Please select a valid project number.")
        }
        return projects[selectedIndex - 1]
    }

    private fun confirmStateAddition(ui: UiDisplayer, inputReader: InputReader, project: Project): Boolean {
        ui.displayMessage("⚠️ Add state to project '${project.name}' (ID: ${project.projectId})? [y/n]: ")
        val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
        return confirmation == "y" || confirmation == "yes"
    }

    private suspend fun addStateToProject(ui: UiDisplayer, inputReader: InputReader, project: Project) {
        val stateName = readNonBlankInput(
            ui,
            inputReader,
            "🔹 Enter state name:",
            "State Name",
            "State name must not be blank"
        )
        ui.displayMessage("🔹 Adding state to project '${project.name}'...")
        addProjectStatesUseCase.execute(projectId = project.projectId, stateName = stateName)
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
            else -> "❌ An unexpected error occurred: ${exception.message ?: "Failed to add state to project"}"
        }
        ui.displayMessage(message)
    }
}