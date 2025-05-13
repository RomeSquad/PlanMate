package org.example.presentation.task

import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.history.AddChangeHistoryUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.state.EditProjectStateUseCase
import org.example.logic.usecase.task.EditTaskUseCase
import org.example.logic.usecase.task.GetTasksByProjectIdUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.util.Date
import java.util.UUID

class EditTaskUI(
    private val editTaskUseCase: EditTaskUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val editProjectStateUseCase: EditProjectStateUseCase,
    private val addChangeHistory: AddChangeHistoryUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║    Task Edit Menu        ║
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
                "Select the number of project that you want to edit task in: ",
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
                "Select the number of task that you want to edit: ",
                1..tasks.size
            )?.minus(1) ?: throw IllegalArgumentException("Invalid task selection.")
            val selectedTask = tasks[taskIndex]
            ui.displayMessage("🔹 Enter New Task Title (leave empty to keep current):")
            val newTitle = inputReader.readString("New Title: ").trim()
            val title = newTitle.ifEmpty { selectedTask.title }
            ui.displayMessage("🔹 Enter New Task Description (leave empty to keep current):")
            val newDescription = inputReader.readString("New Description: ").trim()
            val description = newDescription.ifEmpty { selectedTask.description }
            ui.displayMessage("🔹 Enter New Task State (leave empty to keep current):")
            val newState = inputReader.readString("New State: ").trim()
            val state = newState.ifEmpty { selectedTask.state.stateName }
            editTaskUseCase.editTask(
                taskId = selectedTask.taskId,
                title = title,
                description = description,
                updatedAt = System.currentTimeMillis()
            )
            editProjectStateUseCase.execute(
                projectId = selectedProject.projectId,
                newStateName = state,
            )

            val currentUser = getCurrentUserUseCase.getCurrentUser()
            addChangeHistory.execute(
                projectId = selectedProject.projectId,
                taskId = selectedTask.taskId,
                authorId = currentUser!!.userId,
                changeDate = Date(Date().time) ,
                changeDescription = "Task Updated",
            )
            ui.displayMessage("✅ Task '${selectedTask.title}' updated successfully!")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message}")
        }
    }
}