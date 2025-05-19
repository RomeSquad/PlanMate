package org.example.presentation.project

import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import org.example.logic.entity.auth.User
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.history.AddChangeHistoryUseCase
import org.example.logic.usecase.project.InsertProjectUseCase
import org.example.logic.usecase.state.AddProjectStatesUseCase
import org.example.logic.usecase.state.DefaultProjectStateUseCase
import org.example.logic.usecase.task.CreateTaskUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.util.*

class CreateProjectUi(
    private val insertProjectUseCase: InsertProjectUseCase,
    private val defaultProjectStateUseCase: DefaultProjectStateUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val addProjectStatesUseCase: AddProjectStatesUseCase,
    private val addChangeHistory: AddChangeHistoryUseCase
) : MenuAction {

    override val description: String = buildCreateProjectDescription()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runCatching {
            ui.displayMessage(description)
            val currentUser = getCurrentUserUseCase.getCurrentUser()
                ?: throw IllegalStateException("No authenticated user found. Please log in.")

            val projectInfo = collectProjectInfo(ui, inputReader)
            val initialState = createInitialProjectState(ui, inputReader)
            val project = createProject(projectInfo, initialState)
            val projectId = saveProject(project, currentUser)
            logProjectCreation(projectId, currentUser.userId)

            ui.displayMessage("✅ Project '${projectInfo.name}' created successfully! 🎉")
            initializeDefaultProjectState(projectId)
            handleTasksCreation(ui, inputReader, projectId)

            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }.onFailure { exception ->
            handleError(ui, exception)
        }
    }

    private fun buildCreateProjectDescription(): String = """
        ╔════════════════════════════╗
        ║    Create a New Project    ║
        ╚════════════════════════════╝
    """.trimIndent()

    private data class ProjectInfo(val name: String, val description: String)

    private fun collectProjectInfo(ui: UiDisplayer, inputReader: InputReader): ProjectInfo {
        val name = readNonBlankInput(
            ui,
            inputReader,
            "🔹 Enter project name:",
            "Project Name",
            "Project name must not be blank"
        )
        val description = readNonBlankInput(
            ui,
            inputReader,
            "🔹 Enter project description:",
            "Project Description",
            "Project description must not be blank"
        )
        return ProjectInfo(name, description)
    }

    private suspend fun createInitialProjectState(ui: UiDisplayer, inputReader: InputReader): ProjectState {
        val stateName = readNonBlankInput(
            ui,
            inputReader,
            "🔹 Enter initial project state:",
            "State Name",
            "State name must not be blank"
        )
        val stateId = UUID.randomUUID()
        val projectState = ProjectState(projectId = stateId, stateName = stateName)
        addProjectStatesUseCase.execute(stateName, stateId)
        return projectState
    }

    private fun createProject(info: ProjectInfo, state: ProjectState): Project {
        return Project(
            projectId = UUID.randomUUID(),
            name = info.name,
            description = info.description,
            state = state
        )
    }

    private suspend fun saveProject(project: Project, currentUser: User): UUID {
        return insertProjectUseCase.insertProject(project, currentUser)
    }

    private suspend fun logProjectCreation(projectId: UUID, userId: UUID) {
        addChangeHistory.execute(
            projectId = projectId,
            taskId = UUID.randomUUID(),
            authorId = userId,
            changeDate = Date(),
            changeDescription = "Project created"
        )
    }

    private suspend fun initializeDefaultProjectState(projectId: UUID) {
        defaultProjectStateUseCase.initializeProjectState(projectId)
    }

    private suspend fun handleTasksCreation(ui: UiDisplayer, inputReader: InputReader, projectId: UUID) {
        if (shouldCreateTask(ui, inputReader)) {
            createTasksLoop(ui, inputReader, projectId)
        }
    }

    private fun shouldCreateTask(ui: UiDisplayer, inputReader: InputReader): Boolean {
        ui.displayMessage("🔹 Do you want to create a task for this project? (y/n)")
        return inputReader.readString("Create Task (y/n): ").trim().lowercase() == "y"
    }

    private suspend fun createTasksLoop(ui: UiDisplayer, inputReader: InputReader, projectId: UUID) {
        while (true) {
            val task = collectTaskInfo(ui, inputReader, projectId)
            createTaskUseCase.createTask(task)
            ui.displayMessage("✅ Task '${task.title}' created successfully! 🎉")
            if (!shouldCreateAnotherTask(ui, inputReader)) break
        }
    }

    private data class TaskInfo(val title: String, val description: String)

    private suspend fun collectTaskInfo(ui: UiDisplayer, inputReader: InputReader, projectId: UUID): Task {
        val taskInfo = TaskInfo(
            title = readNonBlankInput(
                ui,
                inputReader,
                "🔹 Enter task title:",
                "Task Title",
                "Task title must not be blank"
            ),
            description = readNonBlankInput(
                ui,
                inputReader,
                "🔹 Enter task description:",
                "Task Description",
                "Task description must not be blank"
            )
        )
        val taskState = createTaskState(ui, inputReader)
        return Task(
            taskId = UUID.randomUUID(),
            title = taskInfo.title,
            description = taskInfo.description,
            projectId = projectId,
            state = taskState,
            createdBy = UUID.randomUUID(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }

    private suspend fun createTaskState(ui: UiDisplayer, inputReader: InputReader): ProjectState {
        val stateName = readNonBlankInput(
            ui,
            inputReader,
            "🔹 Enter task state:",
            "Task State Name",
            "Task state name must not be blank"
        )
        val stateId = UUID.randomUUID()
        val taskState = ProjectState(projectId = stateId, stateName = stateName)
        addProjectStatesUseCase.execute(stateName, stateId)
        return taskState
    }

    private fun shouldCreateAnotherTask(ui: UiDisplayer, inputReader: InputReader): Boolean {
        ui.displayMessage("🔹 Do you want to create another task? (y/n)")
        return inputReader.readString("Create Another Task (y/n): ").trim().lowercase() == "y"
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
            else -> "❌ An unexpected error occurred: ${exception.message ?: "Failed to create project"}"
        }
        ui.displayMessage(message)
    }
}