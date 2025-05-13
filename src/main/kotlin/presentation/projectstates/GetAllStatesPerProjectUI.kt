package org.example.presentation.projectstates

import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.state.GetAllProjectStatesUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class GetAllStatesPerProjectUI(
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getAllProjectStatesUseCase: GetAllProjectStatesUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║ States per Project Menu  ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Available Projects:")
            val projects = getAllProjectsUseCase.getAllProjects()
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available!")
                return
            }
            projects.forEachIndexed { index, project ->
                ui.displayMessage("📂 ${index + 1}. ${project.name} | 🆔 ID: ${project.projectId}")
            }
            val projectIndex = inputReader.readIntOrNull(
                "🔹 Choose a project to view its states:", 1..projects.size
            )?.minus(1)
                ?: throw IllegalArgumentException("Invalid project selection.")
            val selectedProject = projects[projectIndex]
            val states = getAllProjectStatesUseCase.execute(
                state = selectedProject.state
            )
            if (states.isEmpty()) {
                ui.displayMessage("⚠️ No states found for project '${selectedProject.name}'.")
            } else {
                ui.displayMessage("📌 States in Project: ${selectedProject.name}")
                states.forEach { state ->
                    ui.displayMessage("✅ ${state.stateName} | 🏷️ ID: ${state.projectId}")
                }
            }
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message}")
        }
    }
}