package org.example.presentation.task

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
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔍 Fetching all tasks...")
            val projects = getAllProjectsUseCase.getAllProjects()
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("📂 Available Projects:")
            projects.forEachIndexed { index, project ->
                ui.displayMessage("📂 ${index + 1}. ${project.name} | 🆔 ID: ${project.projectId}")
            }
            ui.displayMessage("🔹 Choose a project to view tasks:")
            val projectIndex = inputReader.readString("🔹 Enter project number: ").toIntOrNull()
            if (projectIndex == null || projectIndex < 1 || projectIndex > projects.size) {
                ui.displayMessage("❌ Invalid project selection.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            val selectedProject = projects[projectIndex - 1]
            ui.displayMessage("📂 Selected Project: ${selectedProject.name} | 🆔 ID: ${selectedProject.projectId}")
            ui.displayMessage("🔍 Fetching tasks for project ${selectedProject.name}...")
            val tasks = getTasksByProjectIdUseCase.getTasksByProjectId(selectedProject.projectId)
            if (tasks.isEmpty()) {
                ui.displayMessage("❌ No tasks available for this project.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("📋 Tasks for Project ${selectedProject.name}:")
            tasks.forEach { task ->
                ui.displayMessage(
                    "📝 Task ID: ${task.taskId} | Title: ${task.title} | Created At: ${
                        dateFormatter.format(
                            task.createdAt
                        )
                    }"
                )
            }
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to retrieve tasks"}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}