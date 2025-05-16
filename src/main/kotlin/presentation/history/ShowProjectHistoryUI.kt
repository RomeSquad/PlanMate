package org.example.presentation.history

import org.example.logic.entity.Project
import org.example.logic.usecase.history.ShowProjectHistoryUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.formatter.CliFormatter
import org.example.presentation.utils.formatter.dataFormatter.format
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.util.UUID

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
            val projects = fetchAndDisplayProjects(ui) ?: return
            val selectedProject = selectProject(projects, inputReader, ui) ?: return
            displayProjectHistory(selectedProject.projectId, selectedProject.name, ui)
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to retrieve project"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }

    private suspend fun fetchAndDisplayProjects(ui: UiDisplayer): List<Project>? {
        ui.displayMessage(description)
        ui.displayMessage("🔍 Fetching all projects...")
        val projects = getAllProjectsUseCase.getAllProjects()
        if (projects.isEmpty()) {
            ui.displayMessage("❌ No projects available.")
            return null
        }
        ui.displayMessage("📂 Available Projects:")
        projects.forEachIndexed { index, project ->
            ui.displayMessage("📌 ${index + 1}. ${project.name} | 🆔 ID: ${project.projectId}")
        }
        return projects

    }
    private fun selectProject(projects: List<Project>, inputReader: InputReader, ui: UiDisplayer): Project? {
        val index = inputReader.readIntOrNull("🔹 Select a project to view history:", 1..projects.size)
            ?.minus(1)
            ?: run {
                ui.displayMessage("❌ Invalid project selection.")
                return null
            }
        return projects[index]
    }

    private suspend fun displayProjectHistory(projectId: UUID, projectName: String, ui: UiDisplayer) {
        ui.displayMessage("🔍 Fetching change history for project '$projectName'...")
        val logs = showProjectHistoryUseCase.execute(projectId)
        if (logs.isEmpty()) {
            ui.displayMessage("❌ No change history found for project '$projectName'.")
            return
        }

        val formattedLogs = logs.map { it.format() }
        val formatter = CliFormatter()
        val historyOutput = formatter.verticalLayout(
            messages = formattedLogs,
            width = 100,
            height = 2
        )

        ui.displayMessage("📜 Change History for Project: '$projectName'")
        ui.displayMessage("✅ Change History Details:")
        ui.displayMessage(historyOutput)
    }

}




