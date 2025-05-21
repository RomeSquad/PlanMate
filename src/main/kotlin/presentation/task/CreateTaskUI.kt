package org.example.presentation.task

import logic.usecase.project.EditProjectUseCase
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import org.example.logic.entity.auth.User
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.history.AddChangeHistoryUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.state.AddProjectStatesUseCase
import org.example.logic.usecase.task.CreateTaskUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction
import java.util.*


class CreateTaskUI(
    private val createTaskUseCase: CreateTaskUseCase,
    private val editProjectUseCase: EditProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val addProjectStatesUseCase: AddProjectStatesUseCase,
    private val addChangeHistory: AddChangeHistoryUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : BaseMenuAction() {

    override val title: String = "Create a New Task"

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
            val taskInfo = collectTaskInfo(ui, inputReader)
            val taskState = createTaskState(ui, inputReader, selectedProject.projectId)
            val task = createTask(taskInfo, selectedProject.projectId, taskState)
            if (!confirmAction(
                    ui, inputReader,
                    "⚠️ Create task '${task.title}' for project '${selectedProject.name}'? [y/n]: "
                )
            ) {
                ui.displayMessage("🛑 Task creation canceled.")
                return@executeWithErrorHandling
            }
            saveTask(task)
            updateProjectState(selectedProject, taskState)
            logTaskCreation(task, selectedProject.projectId, currentUser)
            ui.displayMessage("✅ Task '${task.title}' created successfully! 🎉")
        }
    }

    private data class TaskInfo(val title: String, val description: String)

    private fun collectTaskInfo(ui: UiDisplayer, inputReader: InputReader): TaskInfo {
        return TaskInfo(
            title = readValidatedInput(
                ui, inputReader, "🔹 Enter Task Title:", "Title", "Task title must not be blank",
                ::nonBlankValidator
            ),
            description = readValidatedInput(
                ui, inputReader, "🔹 Enter Task Description:", "Description", "Task description must not be blank",
                ::nonBlankValidator
            )
        )
    }

    private fun createTaskState(ui: UiDisplayer, inputReader: InputReader, projectId: UUID): ProjectState {
        val stateName = readValidatedInput(
            ui, inputReader, "🔹 Enter Task State:", "State Name", "State name must not be blank",
            { it.takeIf { it.isNotBlank() } ?: "TODO" }, hint = "leave empty for 'TODO'"
        )
        return ProjectState(projectId = projectId, stateName = stateName)
    }

    private fun createTask(taskInfo: TaskInfo, projectId: UUID, taskState: ProjectState): Task {
        return Task(
            taskId = UUID.randomUUID(),
            title = taskInfo.title,
            description = taskInfo.description,
            state = taskState,
            projectId = projectId,
            createdBy = UUID.randomUUID(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }

    private suspend fun saveTask(task: Task) {
        createTaskUseCase.createTask(task)
        addProjectStatesUseCase.execute(task.state.stateName, task.state.projectId)
    }

    private suspend fun updateProjectState(project: Project, taskState: ProjectState) {
        val updatedProject = project.copy(state = taskState.copy(projectId = project.projectId))
        editProjectUseCase.execute(updatedProject)
    }

    private suspend fun logTaskCreation(task: Task, projectId: UUID, user: User) {
        addChangeHistory.execute(
            projectId = projectId,
            taskId = task.taskId,
            authorId = user.userId,
            changeDate = Date(task.createdAt),
            changeDescription = "Task Created"
        )
    }
}