package org.example.presentation.task

import org.example.logic.entity.Task
import org.example.logic.entity.auth.User
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.history.AddChangeHistoryUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.task.DeleteTaskUseCase
import org.example.logic.usecase.task.GetTasksByProjectIdUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction
import java.util.*

class DeleteTaskUI(
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val addChangeHistory: AddChangeHistoryUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : BaseMenuAction() {

    override val title: String = "Task Deletion Menu"

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
                prompt = "🔹 Select a task to delete (1-${tasks.size}): ",
                format = { task, index -> "📋 $index. ${task.title} | Status: ${task.state.stateName} | ID: ${task.taskId}" }
            ) ?: run {
                ui.displayMessage("❌ No tasks available for project '${selectedProject.name}'.")
                return@executeWithErrorHandling
            }
            if (!confirmAction(
                    ui, inputReader,
                    "⚠️ Are you sure you want to delete '${selectedTask.title}'? [y/n]: "
                )
            ) {
                ui.displayMessage("🛑 Task deletion canceled.")
                return@executeWithErrorHandling
            }
            deleteTaskUseCase.deleteTask(selectedProject.projectId, selectedTask.taskId)
            logTaskDeletion(selectedProject.projectId, selectedTask, currentUser)
            ui.displayMessage("✅ Task '${selectedTask.title}' deleted successfully! 🎉")
        }
    }

    private suspend fun logTaskDeletion(projectId: UUID, task: Task, user: User) {
        addChangeHistory.execute(
            projectId = projectId,
            taskId = task.taskId,
            authorId = user.userId,
            changeDate = Date(),
            changeDescription = "Task Deleted"
        )
    }
}