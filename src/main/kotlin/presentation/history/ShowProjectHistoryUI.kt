package org.example.presentation.history

import org.example.logic.usecase.history.ShowProjectHistoryUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.formatter.CliFormatter
import org.example.presentation.utils.formatter.dataFormatter.format
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class ShowProjectHistoryUI(
    private val showProjectHistoryUseCase: ShowProjectHistoryUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
) : MenuAction {

    override val description: String = """
        ╔════════════════════════════════════╗
        ║      Get Project History by ID     ║
        ╚════════════════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔍 Fetching all projects...")
            val projects = getAllProjectsUseCase.getAllProjects()
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available.")
                return
            }
            ui.displayMessage("📂 Available Projects:")
            projects.forEachIndexed { index, project ->
                ui.displayMessage("📌 ${index + 1}. ${project.name} | 🆔 ID: ${project.projectId}")
            }
            val projectIndex = inputReader.readIntOrNull("🔹 Select a project to view history:", 1..projects.size)
                ?.minus(1)
                ?: throw IllegalArgumentException("Invalid project selection.")
            val selectedProject = projects[projectIndex]
            ui.displayMessage("🔍 Fetching change history for project '${selectedProject.name}'...")
            val logs = showProjectHistoryUseCase.execute(selectedProject.projectId)
            if (logs.isEmpty()) {
                ui.displayMessage("❌ No change history found for project '${selectedProject.name}'.")
                return
            }
            val result = logs.map { it.format() }
            if (result.isEmpty()) {
                ui.displayMessage("❌ No change history found for project '${selectedProject.name}'.")
                return
            }
            val formatter = CliFormatter()
            ui.displayMessage("📜 Change History for Project: '${selectedProject.name}'")
            ui.displayMessage("🔍 Change History Details:")
            val show = formatter.verticalLayout(result.map { it.format() })
            ui.displayMessage("✅ Change History Details:")
            ui.displayMessage(show)
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to retrieve project"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}

