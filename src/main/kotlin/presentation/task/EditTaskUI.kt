package org.example.presentation.task

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
import org.example.presentation.utils.menus.BaseMenuAction
import java.util.*

class EditTaskUI(
    private val editTaskUseCase: EditTaskUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val editProjectStateUseCase: EditProjectStateUseCase,
    private val addChangeHistory: AddChangeHistoryUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : BaseMenuAction() {

    override val title: String = "Task Edit Menu"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val currentUser = getCurrentUser(getCurrentUserUseCase)
            if (currentUser == null) {
                ui.displayMessage("❌ User not logged in.")
                return@executeWithErrorHandling
            }
            val projects = fetchEntities(ui, { getAllProjectsUseCase.getAllProjects() }, "projects")
            val selectedProject = selectEntity(
                ui, inputReader, projects, "Projects",
                format = { project, index -> "📌 $index. ${project.name} | ID: ${project.projectId}" }
            ) ?: run {
                ui.displayMessage("❌ No projects available. Please create a project first.")
                return@executeWithErrorHandling
            }
            val tasks =
                fetchTasksForProject(ui, selectedProject, { id -> getTasksByProjectIdUseCase.getTasksByProjectId(id) })
            val selectedTask = selectEntity(
                ui, inputReader, tasks, "Tasks",
                prompt = "🔹 Select a task to edit (1-${tasks.size}): ",
                format = { task, index -> "📋 $index. ${task.title} | Status: ${task.state.stateName} | ID: ${task.taskId}" }
            ) ?: run {
                ui.displayMessage("❌ No tasks available for project '${selectedProject.name}'.")
                return@executeWithErrorHandling
            }
            val taskUpdates = collectTaskUpdates(ui, inputReader, selectedTask)
            if (taskUpdates.title == selectedTask.title && taskUpdates.description == selectedTask.description && taskUpdates.state == selectedTask.state.stateName) {
                ui.displayMessage("🛑 No changes made to task.")
                return@executeWithErrorHandling
            }
            if (!confirmAction(
                    ui, inputReader,
                    "⚠️ Update task '${selectedTask.title}' in project '${selectedProject.name}'? [y/n]: "
                )
            ) {
                ui.displayMessage("🛑 Task update canceled.")
                return@executeWithErrorHandling
            }
            editTaskUseCase.editTask(
                taskId = selectedTask.taskId,
                title = taskUpdates.title,
                description = taskUpdates.description,
                updatedAt = System.currentTimeMillis()
            )
            if (taskUpdates.state != selectedTask.state.stateName) {
                editProjectStateUseCase.execute(selectedProject.projectId, taskUpdates.state)
            }
            logTaskUpdate(selectedProject.projectId, selectedTask.taskId, currentUser)
            ui.displayMessage("✅ Task '${taskUpdates.title}' updated successfully! 🎉")
        }
    }

    private data class TaskUpdates(val title: String, val description: String, val state: String)

    private fun collectTaskUpdates(ui: UiDisplayer, inputReader: InputReader, task: Task): TaskUpdates {
        return TaskUpdates(
            title = readValidatedInput(
                ui, inputReader, "🔹 Enter New Task Title:", "New Title",
                "Invalid title", { it.takeIf { it.isNotBlank() } ?: task.title },
                hint = "leave empty to keep '${task.title}'"
            ),
            description = readValidatedInput(
                ui, inputReader, "🔹 Enter New Task Description:", "New Description",
                "Invalid description", { it.takeIf { it.isNotBlank() } ?: task.description },
                hint = "leave empty to keep current"
            ),
            state = readValidatedInput(
                ui, inputReader, "🔹 Enter New Task State:", "New State",
                "Invalid state", { it.takeIf { it.isNotBlank() } ?: task.state.stateName },
                hint = "leave empty to keep '${task.state.stateName}'"
            )
        )
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
}