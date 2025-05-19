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
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.util.*


class CreateTaskUI(
    private val createTaskUseCase: CreateTaskUseCase,
    private val editProjectUseCase: EditProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val addProjectStatesUseCase: AddProjectStatesUseCase,
    private val addChangeHistory: AddChangeHistoryUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : MenuAction {

    override val description: String = """
        ╔══════════════════════════╗
        ║    Create a New Task     ║
        ╚══════════════════════════╝
    """.trimIndent()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runCatching {
            ui.displayMessage(description)
            val taskInfo = collectTaskInfo(ui, inputReader)
            val projects = fetchProjects(ui)
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available. Please create a project first.")
                return@runCatching
            }
            val selectedProject =
                selectProject(ui, inputReader, projects) ?: throw IllegalArgumentException("Invalid project selection.")
            val taskState = createTaskState(selectedProject.projectId)
            val task = createTask(taskInfo, selectedProject.projectId, taskState)
            if (!confirmTaskCreation(ui, inputReader, task.title, selectedProject.name)) {
                ui.displayMessage("🛑 Task creation canceled.")
                return@runCatching
            }
            saveTask(task)
            updateProjectState(selectedProject, taskState)
            logTaskCreation(task, selectedProject.projectId, getCurrentUser())
            ui.displayMessage("✅ Task '${task.title}' created successfully!")
            // Prompt to continue only on success
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }.onFailure { exception ->
            handleError(ui, exception)
        }
    }

    private fun collectTaskInfo(ui: UiDisplayer, inputReader: InputReader): TaskInfo {
        val title = readNonBlankInput(ui, inputReader, "🔹 Enter Task Title:", "Title", "Task title must not be blank")
        val description = readNonBlankInput(
            ui,
            inputReader,
            "🔹 Enter Task Description:",
            "Description",
            "Task description must not be blank"
        )
        return TaskInfo(title, description)
    }

    private data class TaskInfo(val title: String, val description: String)

    private suspend fun fetchProjects(ui: UiDisplayer): List<Project> {
        ui.displayMessage("🔹 Fetching all projects...")
        return getAllProjectsUseCase.getAllProjects()
    }

    private fun selectProject(ui: UiDisplayer, inputReader: InputReader, projects: List<Project>): Project? {
        ui.displayMessage("🔹 Select a Project:")
        projects.forEachIndexed { index, project ->
            ui.displayMessage("📂 ${index + 1}. ${project.name} (ID: ${project.projectId})")
        }
        ui.displayMessage("🔹 Please enter a number to choose a project.")
        val projectIndex = inputReader.readIntOrNull(
            string = "",
            ints = 1..projects.size
        )?.minus(1)
        return if (projectIndex != null && projectIndex in projects.indices) projects[projectIndex] else null
    }

    private fun createTaskState(projectId: UUID): ProjectState {
        return ProjectState(projectId = projectId, stateName = "TODO")
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

    private fun confirmTaskCreation(
        ui: UiDisplayer,
        inputReader: InputReader,
        taskTitle: String,
        projectName: String
    ): Boolean {
        ui.displayMessage("⚠️ Create task '$taskTitle' for project '$projectName'? [y/n]: ")
        val confirmation = inputReader.readString("").trim().lowercase()
        return confirmation == "y"
    }

    private suspend fun saveTask(task: Task) {
        createTaskUseCase.createTask(task)
        addProjectStatesUseCase.execute(task.state.stateName, task.state.projectId)
    }

    private suspend fun updateProjectState(project: Project, taskState: ProjectState) {
        val updatedProject = project.copy(state = taskState.copy(projectId = project.projectId))
        editProjectUseCase.execute(updatedProject)
    }

    private suspend fun getCurrentUser(): User {
        return getCurrentUserUseCase.getCurrentUser()
            ?: throw IllegalStateException("No authenticated user found. Please log in.")
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

    private fun readNonBlankInput(
        ui: UiDisplayer,
        inputReader: InputReader,
        prompt: String,
        label: String,
        errorMessage: String
    ): String {
        ui.displayMessage(prompt)
        val input = inputReader.readString("$label: ").trim()
        if (input.isBlank()) throw IllegalArgumentException(errorMessage)
        return input
    }

    private fun handleError(ui: UiDisplayer, exception: Throwable) {
        val message = when (exception) {
            is IllegalArgumentException -> "❌ Error: ${exception.message}"
            else -> "❌ An unexpected error occurred: ${exception.message ?: "Failed to create task"}"
        }
        ui.displayMessage(message)
    }
}