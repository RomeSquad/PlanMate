package org.example.presentation.project

import org.example.logic.entity.Project
import org.example.logic.usecase.project.DeleteProjectByIdUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class DeleteProjectUi(
    private val deleteProjectUseCase: DeleteProjectByIdUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : MenuAction {

    override val description: String = """
        ╔════════════════════════════╗
        ║    Delete a Project        ║
        ╚════════════════════════════╝
    """.trimIndent()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runCatching {
            ui.displayMessage(description)
            val projects = fetchProjects(ui)
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available for deletion.")
                return@runCatching
            }
            displayProjects(ui, projects)
            val selectedProject = selectProject(ui, inputReader, projects) ?: return@runCatching
            if (!confirmDeletion(ui, inputReader)) {
                ui.displayMessage("❌ Deletion canceled.")
                return@runCatching
            }
            deleteProject(ui, selectedProject)
            ui.displayMessage("✅ Project '${selectedProject.name}' deleted successfully!")
            // Prompt to continue only on success
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }.onFailure { exception ->
            handleError(ui, exception)
        }
    }

    private suspend fun fetchProjects(ui: UiDisplayer): List<Project> {
        ui.displayMessage("🔹 Fetching all projects...")
        return getAllProjectsUseCase.getAllProjects()
    }

    private fun displayProjects(ui: UiDisplayer, projects: List<Project>) {
        ui.displayMessage("📂 Available Projects:")
        projects.forEachIndexed { index, project ->
            ui.displayMessage("📌 ${index + 1}. ${project.name} | 🆔 ID: ${project.projectId}")
        }
        ui.displayMessage("🔹 Please enter a number to choose a project.")
    }

    private fun selectProject(ui: UiDisplayer, inputReader: InputReader, projects: List<Project>): Project? {
        ui.displayMessage("🔹 Select a project to delete (1-${projects.size}): ")
        val projectIndex = inputReader.readIntOrNull(
            string = "",
            ints = 1..projects.size
        )?.minus(1)
        if (projectIndex == null || projectIndex !in projects.indices) {
            ui.displayMessage("❌ Invalid selection. Please try again.")
            return null
        }
        val selectedProject = projects[projectIndex]
        ui.displayMessage("🔹 You selected: ${selectedProject.name} | 🆔 ID: ${selectedProject.projectId}")
        return selectedProject
    }

    private fun confirmDeletion(ui: UiDisplayer, inputReader: InputReader): Boolean {
        ui.displayMessage("⚠️ Are you sure you want to delete this project? [y/n]: ")
        val confirmation = inputReader.readString("").trim().lowercase()
        return confirmation == "y" || confirmation == "yes"
    }

    private suspend fun deleteProject(ui: UiDisplayer, project: Project) {
        ui.displayMessage("🔄 Deleting project...")
        deleteProjectUseCase.deleteProjectById(project.projectId)
        ui.displayMessage("🔹 Deleting project '${project.name}'...")
    }

    private fun handleError(ui: UiDisplayer, exception: Throwable) {
        val message = when (exception) {
            is IllegalArgumentException -> "❌ Error: ${exception.message}"
            else -> "❌ An unexpected error occurred: ${exception.message ?: "Failed to delete project"}"
        }
        ui.displayMessage(message)
    }
}