package org.example.presentation.task

import org.example.logic.entity.Project
import org.example.logic.entity.Task
import org.example.logic.entity.auth.User
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
import java.util.*

class EditTaskUI(
    private val editTaskUseCase: EditTaskUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val editProjectStateUseCase: EditProjectStateUseCase,
    private val addChangeHistory: AddChangeHistoryUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : MenuAction {

    override val description: String = """
        ╔══════════════════════════╗
        ║    Task Edit Menu        ║
        ╚══════════════════════════╝
    """.trimIndent()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runCatching {
            ui.displayMessage(description)
            val projects = fetchProjects(ui)
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available. Please create a project first.")
                return@runCatching
            }
            val selectedProject =
                selectProject(ui, inputReader, projects) ?: throw IllegalArgumentException("Invalid project selection.")
            val tasks = fetchTasks(ui, selectedProject)
            if (tasks.isEmpty()) {
                ui.displayMessage("❌ No tasks available for project '${selectedProject.name}'.")
                return@runCatching
            }
            val selectedTask =
                selectTask(ui, inputReader, tasks) ?: throw IllegalArgumentException("Invalid task selection.")
            val taskUpdates = collectTaskUpdates(ui, inputReader, selectedTask)
            editTask(selectedTask.taskId, taskUpdates)
            updateProjectState(selectedProject.projectId, taskUpdates.state)
            logTaskUpdate(selectedProject.projectId, selectedTask.taskId, getCurrentUser())
            ui.displayMessage("✅ Task '${taskUpdates.title}' updated successfully!")
            // Prompt to continue only on success
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }.onFailure { exception ->
            handleError(ui, exception)
        }
    }

    private suspend fun fetchProjects(ui: UiDisplayer): List<Project> {
        ui.displayMessage("🔹 Fetching all projects...")
        return getAllProjectsUseCase.getAllProjects()
    }

    private fun selectProject(ui: UiDisplayer, inputReader: InputReader, projects: List<Project>): Project? {
        ui.displayMessage("📂 Available Projects:")
        projects.forEachIndexed { index, project ->
            ui.displayMessage("📌 ${index + 1}. ${project.name} (ID: ${project.projectId})")
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
        ui.displayMessage("🔹 Fetching tasks for project '${project.name}'...")
        return getTasksByProjectIdUseCase.getTasksByProjectId(project.projectId)
    }

    private fun selectTask(ui: UiDisplayer, inputReader: InputReader, tasks: List<Task>): Task? {
        ui.displayMessage("✅ Available Tasks:")
        tasks.forEachIndexed { index, task ->
            ui.displayMessage("📋 ${index + 1}. ${task.title} (ID: ${task.taskId})")
        }
        ui.displayMessage("🔹 Please enter a number to choose a task.")
        ui.displayMessage("🔹 Select a task to edit (1-${tasks.size}): ")
        val taskIndex = inputReader.readIntOrNull(
            string = "",
            ints = 1..tasks.size
        )?.minus(1)
        return if (taskIndex != null && taskIndex in tasks.indices) tasks[taskIndex] else null
    }

    private data class TaskUpdates(val title: String, val description: String, val state: String)

    private fun collectTaskUpdates(ui: UiDisplayer, inputReader: InputReader, task: Task): TaskUpdates {
        ui.displayMessage("🔹 Enter New Task Title (leave empty to keep current):")
        val newTitle = inputReader.readString("New Title: ").trim()
        val title = newTitle.ifEmpty { task.title }

        ui.displayMessage("🔹 Enter New Task Description (leave empty to keep current):")
        val newDescription = inputReader.readString("New Description: ").trim()
        val description = newDescription.ifEmpty { task.description }

        ui.displayMessage("🔹 Enter New Task State (leave empty to keep current):")
        val newState = inputReader.readString("New State: ").trim()
        val state = newState.ifEmpty { task.state.stateName }

        return TaskUpdates(title, description, state)
    }

    private suspend fun editTask(taskId: UUID, updates: TaskUpdates) {
        editTaskUseCase.editTask(
            taskId = taskId,
            title = updates.title,
            description = updates.description,
            updatedAt = System.currentTimeMillis()
        )
    }

    private suspend fun updateProjectState(projectId: UUID, state: String) {
        editProjectStateUseCase.execute(projectId, state)
    }

    private suspend fun getCurrentUser(): User {
        return getCurrentUserUseCase.getCurrentUser()
            ?: throw IllegalStateException("No authenticated user found. Please log in.")
    }

    private suspend fun logTaskUpdate(projectId: UUID, taskId: UUID, user: User) {
        addChangeHistory.execute(
            projectId = projectId,
            taskId = taskId,
            authorId = user.userId,
            changeDate = Date(),
            changeDescription = "Task Updated"
        )
    }

    private fun handleError(ui: UiDisplayer, exception: Throwable) {
        val message = when (exception) {
            is IllegalArgumentException -> "❌ Error: ${exception.message}"
            else -> "❌ An unexpected error occurred: ${exception.message ?: "Failed to edit task"}"
        }
        ui.displayMessage(message)
    }
}