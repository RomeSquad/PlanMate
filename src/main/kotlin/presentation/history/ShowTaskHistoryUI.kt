package org.example.presentation.history

import org.example.logic.usecase.history.ShowTaskHistoryUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.task.GetTasksByProjectIdUseCase
import org.example.presentation.utils.formatter.CliFormatter
import org.example.presentation.utils.formatter.dataFormatter.format
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class ShowTaskHistoryUI(
    private val showTaskHistoryUseCase: ShowTaskHistoryUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
) : MenuAction {

    override val description: String = """
        ╔════════════════════════════════════╗
        ║        Get Task History by ID      ║
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
            val projectIndex = inputReader.readIntOrNull("🔹 Select a project to view task history:", 1..projects.size)
                ?.minus(1)
                ?: throw IllegalArgumentException("Invalid project selection.")
            val selectedProject = projects[projectIndex]
            ui.displayMessage("🔍 Fetching tasks for project '${selectedProject.name}'...")
            val tasks = getTasksByProjectIdUseCase.getTasksByProjectId(selectedProject.projectId)
            if (tasks.isEmpty()) {
                ui.displayMessage("❌ No tasks found for project '${selectedProject.name}'.")
                return
            }
            ui.displayMessage("📝 Available Tasks:")
            tasks.forEachIndexed { index, task ->
                ui.displayMessage("✅ ${index + 1}. ${task.title} | 🏷️ Status: ${task.state.stateName} | 🆔 ID: ${task.taskId}")
            }
            val taskIndex = inputReader.readIntOrNull("🔹 Select a task to view history:", 1..tasks.size)
                ?.minus(1)
                ?: throw IllegalArgumentException("Invalid task selection.")
            val selectedTask = tasks[taskIndex]
            ui.displayMessage("🔍 Fetching change history for task '${selectedTask.title}'...")
            val result = showTaskHistoryUseCase.execute(selectedTask.taskId)
            ui.displayMessage("🔍 Fetching change history details...")
            if (result.isEmpty()) {
                ui.displayMessage("❌ No change history details found.")
                return
            }
            ui.displayMessage("🔍 Formatting change history details...")
            val formatter = CliFormatter()
            val show = formatter.verticalLayout(
                messages = result.map { it.format() },
                width = 100,
                height = 2
            )
            ui.displayMessage("🔍 Formatting completed.")
            ui.displayMessage("🔍 Displaying change history details...")
            ui.displayMessage("🔍 Change history details retrieved successfully.")
            ui.displayMessage("📜 Change History for '${selectedTask.title}':")
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