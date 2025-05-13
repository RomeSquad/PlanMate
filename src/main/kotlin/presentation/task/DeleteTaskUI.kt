package org.example.presentation.task

import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.history.AddChangeHistoryUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.task.DeleteTaskUseCase
import org.example.logic.usecase.task.GetTasksByProjectIdUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.util.Date
import java.util.UUID

class DeleteTaskUI(
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val addChangeHistory: AddChangeHistoryUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║   Task Deletion Menu     ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            val projects = getAllProjectsUseCase.getAllProjects()
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available. Please create a project first.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("Available Projects:")
            projects.forEachIndexed { index, project ->
                ui.displayMessage("📂 ${index + 1}. ${project.name} (ID: ${project.projectId})")
            }
            val projectIndex = inputReader.readIntOrNull(
                "Select the number of project that you want to delete task from: ",
                1..projects.size
            )?.minus(1) ?: throw IllegalArgumentException("Invalid project selection.")
            val selectedProject = projects[projectIndex]
            val tasks = getTasksByProjectIdUseCase.getTasksByProjectId(selectedProject.projectId)
            if (tasks.isEmpty()) {
                ui.displayMessage("❌ No tasks available for project '${selectedProject.name}'.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("Available Tasks:")
            tasks.forEachIndexed { index, task ->
                ui.displayMessage("✅ ${index + 1}. ${task.title} (ID: ${task.taskId})")
            }
            val taskIndex = inputReader.readIntOrNull(
                "Select the number of task that you want to delete: ",
                1..tasks.size
            )?.minus(1) ?: throw IllegalArgumentException("Invalid task selection.")
            val selectedTask = tasks[taskIndex]
            ui.displayMessage("⚠️ Are you sure you want to delete '${selectedTask.title}'? This action cannot be undone.")
            val confirmation = inputReader.readString("Type 'YES' to confirm: ").trim()
            if (confirmation.equals("YES", ignoreCase = true)) {
                deleteTaskUseCase.deleteTask(selectedProject.projectId, selectedTask.taskId)
                val currentUser = getCurrentUserUseCase.getCurrentUser()
                addChangeHistory.execute(
                    projectId = selectedProject.projectId,
                    taskId = selectedTask.taskId,
                    authorId = currentUser!!.userId,
                    changeDate = Date(Date().time) ,
                    changeDescription = "Task Deleted",
                )
                ui.displayMessage("✅ Task '${selectedTask.title}' deleted successfully!")
            } else {
                ui.displayMessage("❌ Task deletion cancelled.")
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