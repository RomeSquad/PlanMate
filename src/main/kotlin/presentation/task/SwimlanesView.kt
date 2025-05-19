package org.example.presentation.task

import org.example.logic.entity.Project
import org.example.logic.entity.Task
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.task.GetTasksByProjectIdUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.text.SimpleDateFormat


class SwimlanesView(
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : MenuAction {

    override val description: String = """
        ╔══════════════════════════╗
        ║     All Tasks Viewer     ║
        ╚══════════════════════════╝
    """.trimIndent()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runCatching {
            ui.displayMessage(description)
            val projects = fetchProjects(ui)
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available.")
                return@runCatching
            }
            val selectedProject =
                selectProject(ui, inputReader, projects) ?: throw IllegalArgumentException("Invalid project selection.")
            val tasks = fetchTasks(ui, selectedProject)
            if (tasks.isEmpty()) {
                ui.displayMessage("❌ No tasks available for project '${selectedProject.name}'.")
                return@runCatching
            }
            displayTasks(ui, tasks, selectedProject)
            // Prompt to continue only on success
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }.onFailure { exception ->
            handleError(ui, exception)
        }
    }

    private suspend fun fetchProjects(ui: UiDisplayer): List<Project> {
        ui.displayMessage("🔍 Fetching all projects...")
        return getAllProjectsUseCase.getAllProjects()
    }

    private fun selectProject(ui: UiDisplayer, inputReader: InputReader, projects: List<Project>): Project? {
        ui.displayMessage("📂 Available Projects:")
        projects.forEachIndexed { index, project ->
            ui.displayMessage("📌 ${index + 1}. ${project.name} | 🆔 ID: ${project.projectId}")
        }
        ui.displayMessage("🔹 Please enter a number to choose a project.")
        ui.displayMessage("🔹 Select a project (1-${projects.size}): ")
        val projectIndex = inputReader.readIntOrNull(
            string = "",
            ints = 1..projects.size
        )?.minus(1)
        return if (projectIndex != null && projectIndex in projects.indices) projects[projectIndex] else null
    }

    private suspend fun fetchTasks(ui: UiDisplayer, project: Project): List<Task> {
        ui.displayMessage("🔍 Fetching tasks for project '${project.name}'...")
        return getTasksByProjectIdUseCase.getTasksByProjectId(project.projectId)
    }

    private fun displayTasks(ui: UiDisplayer, tasks: List<Task>, project: Project) {
        ui.displayMessage("📋 Tasks for Project '${project.name}':")
        tasks.forEach { task ->
            ui.displayMessage(
                "📝 Task ID: ${task.taskId} | Title: ${task.title} | Created At: ${formatDate(task.createdAt)}"
            )
        }
    }

    private fun formatDate(timestamp: Long): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormatter.format(timestamp)
    }

    private fun handleError(ui: UiDisplayer, exception: Throwable) {
        val message = when (exception) {
            is IllegalArgumentException -> "❌ Error: ${exception.message}"
            else -> "❌ An unexpected error occurred: ${exception.message ?: "Failed to retrieve tasks"}"
        }
        ui.displayMessage(message)
    }
}