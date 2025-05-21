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
import org.example.presentation.utils.menus.BaseMenuAction
import java.util.*

class CreateProjectUI(
    private val insertProjectUseCase: InsertProjectUseCase,
    private val defaultProjectStateUseCase: DefaultProjectStateUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val addProjectStatesUseCase: AddProjectStatesUseCase,
    private val addChangeHistory: AddChangeHistoryUseCase
) : BaseMenuAction() {

    override val title: String = "Create a New Project"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val currentUser = getCurrentUser(getCurrentUserUseCase)
            if (currentUser == null) {
                ui.displayMessage("❌ User not logged in.")
                return@executeWithErrorHandling
            }
            val projectInfo = collectProjectInfo(ui, inputReader)
            val initialState = createInitialProjectState(ui, inputReader)
            val project = createProject(projectInfo, initialState)
            val projectId = saveProject(project, currentUser)
            logProjectCreation(projectId, currentUser.userId)
            ui.displayMessage("✅ Project '${projectInfo.name}' created successfully! 🎉")
            initializeDefaultProjectState(projectId)
            handleTasksCreation(ui, inputReader, projectId, initialState)
        }
    }

    private data class ProjectInfo(val name: String, val description: String)

    private fun collectProjectInfo(ui: UiDisplayer, inputReader: InputReader): ProjectInfo {
        val name = readValidatedInput(
            ui, inputReader, "🔹 Enter project name:", "Project Name", "Project name must not be blank",
            ::nonBlankValidator
        )
        val description = readValidatedInput(
            ui,
            inputReader,
            "🔹 Enter project description:",
            "Project Description",
            "Project description must not be blank",
            ::nonBlankValidator
        )
        return ProjectInfo(name, description)
    }

    private suspend fun createInitialProjectState(ui: UiDisplayer, inputReader: InputReader): ProjectState {
        val stateName = readValidatedInput(
            ui, inputReader, "🔹 Enter initial project state:", "State Name", "State name must not be blank",
            ::nonBlankValidator
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
            taskId = null,
            authorId = userId,
            changeDate = Date(),
            changeDescription = "Project created"
        )
    }

    private suspend fun initializeDefaultProjectState(projectId: UUID) {
        defaultProjectStateUseCase.initializeProjectState(projectId)
    }

    private suspend fun handleTasksCreation(
        ui: UiDisplayer,
        inputReader: InputReader,
        projectId: UUID,
        defaultState: ProjectState
    ) {
        if (confirmAction(ui, inputReader, "🔹 Do you want to create a task for this project? [y/n]: ")) {
            createTasksLoop(ui, inputReader, projectId, defaultState)
        }
    }

    private suspend fun createTasksLoop(
        ui: UiDisplayer,
        inputReader: InputReader,
        projectId: UUID,
        defaultState: ProjectState
    ) {
        while (true) {
            val task = collectTaskInfo(ui, inputReader, projectId, defaultState)
            createTaskUseCase.createTask(task)
            ui.displayMessage("✅ Task '${task.title}' created successfully! 🎉")
            if (!confirmAction(ui, inputReader, "🔹 Do you want to create another task? [y/n]: ")) break
        }
    }

    private fun collectTaskInfo(
        ui: UiDisplayer,
        inputReader: InputReader,
        projectId: UUID,
        defaultState: ProjectState
    ): Task {
        val title = readValidatedInput(
            ui, inputReader, "🔹 Enter task title:", "Task Title", "Task title must not be blank",
            ::nonBlankValidator
        )
        val description = readValidatedInput(
            ui, inputReader, "🔹 Enter task description:", "Task Description", "Task description must not be blank",
            ::nonBlankValidator
        )
        return Task(
            taskId = UUID.randomUUID(),
            title = title,
            description = description,
            projectId = projectId,
            state = defaultState,
            createdBy = UUID.randomUUID(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
    }
}