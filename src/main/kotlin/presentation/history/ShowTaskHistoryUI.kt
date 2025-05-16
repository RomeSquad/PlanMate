package org.example.presentation.history

import org.example.logic.entity.Project
import org.example.logic.entity.Task
import org.example.logic.usecase.history.ShowTaskHistoryUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.task.GetTasksByProjectIdUseCase
import org.example.presentation.utils.formatter.CliFormatter
import org.example.presentation.utils.formatter.dataFormatter.format
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.util.*

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
            val projects = fetchAndDisplayProjects(ui) ?: return
            val selectedProject = selectProject(projects, inputReader, ui) ?: return
            val tasks = fetchAndDisplayTasks(selectedProject.projectId, selectedProject.name, ui) ?: return
            val selectedTask = selectTask(tasks, inputReader, ui) ?: return
            displayTaskHistory(selectedTask.taskId, selectedTask.title, ui)
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to retrieve task history"}")
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
        val index = inputReader.readIntOrNull("🔹 Select a project to view task history:", 1..projects.size)
            ?.minus(1)
            ?: run {
                ui.displayMessage("❌ Invalid project selection.")
                return null
            }
        return projects[index]
    }

    private suspend fun fetchAndDisplayTasks(projectId: UUID, projectName: String, ui: UiDisplayer): List<Task>? {
        ui.displayMessage("🔍 Fetching tasks for project '$projectName'...")
        val tasks = getTasksByProjectIdUseCase.getTasksByProjectId(projectId)
        if (tasks.isEmpty()) {
            ui.displayMessage("❌ No tasks found for project '$projectName'.")
            return null
        }
        ui.displayMessage("📝 Available Tasks:")
        tasks.forEachIndexed { index, task ->
            ui.displayMessage("✅ ${index + 1}. ${task.title} | 🏷️ Status: ${task.state.stateName} | 🆔 ID: ${task.taskId}")
        }
        return tasks

    }

    private fun selectTask(tasks: List<Task>, inputReader: InputReader, ui: UiDisplayer): Task? {
        val index = inputReader.readIntOrNull("🔹 Select a task to view history:", 1..tasks.size)
            ?.minus(1)
            ?: run {
                ui.displayMessage("❌ Invalid task selection.")
                return null
            }

        return tasks[index]
    }

    private suspend fun displayTaskHistory(taskId: UUID, taskTitle: String, ui: UiDisplayer) {
        ui.displayMessage("🔍 Fetching change history for task '$taskTitle'...")

        val history = showTaskHistoryUseCase.execute(taskId)
        if (history.isEmpty()) {
            ui.displayMessage("❌ No change history details found.")
            return
        }

        ui.displayMessage("🔍 Formatting change history details...")
        val formatter = CliFormatter()
        val formatted = formatter.verticalLayout(
            messages = history.map { it.format() },
            width = 100,
            height = 2
        )

        ui.displayMessage("📜 Change History for '$taskTitle':")
        ui.displayMessage(formatted)
    }

    }