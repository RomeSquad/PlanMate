package org.example.presentation.projectstates


import logic.usecase.project.EditProjectUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class EditProjectStateUI(
    private val editProjectUseCase: EditProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║   Edit Project State     ║
        ╚══════════════════════════╝
    """.trimIndent()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Available Projects:")
            val projects = getAllProjectsUseCase.getAllProjects()
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available for editing!")
                return
            }
            projects.forEachIndexed { index, project ->
                ui.displayMessage("📂 ${index + 1}. ${project.name} | 🆔 ID: ${project.projectId}")
            }
            val projectIndex = inputReader.readIntOrNull("🔹 Choose a project to edit its state:", 1..projects.size)
                ?.minus(1)
                ?: throw IllegalArgumentException("Invalid project selection.")
            val selectedProject = projects[projectIndex]
            ui.displayMessage("🔹 Current State: ${selectedProject.state.stateName}")
            val newState = inputReader.readString("🔹 Enter new state name:")
                .takeIf { it.isNotBlank() }
                ?: throw IllegalArgumentException("State name cannot be empty.")
            val updatedProject = selectedProject.copy(state = selectedProject.state.copy(stateName = newState))
            editProjectUseCase.execute(
                project = updatedProject,
            )
            ui.displayMessage("✅ Project '${selectedProject.name}' state updated successfully to '${updatedProject.state.stateName}'!")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")

        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message}")
        }
    }
}