package org.example.presentation.project

import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
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
    private val addChangeHistory: AddChangeHistoryUseCase,

) : MenuAction {

    override val description: String = """
            ╔════════════════════════════╗
            ║    Create a New Project    ║
            ╚════════════════════════════╝
            """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            val currentUser = getCurrentUserUseCase.getCurrentUser()
                ?: throw IllegalArgumentException("No authenticated user found! Please log in first.")
            ui.displayMessage("🔹 Current User: ${currentUser.username}")

            val (name, description) = collectProjectInfo(
                ui = ui,
                inputReader = inputReader
            )
            val projectState = createInitialState(
                ui = ui,
                inputReader = inputReader
            )
            val project = buildProject(
                name = name,
                description = description,
                userId = currentUser.userId,
                state = projectState
            )
            val projectId = insertProjectUseCase.insertProject(
                project = project,
                user = currentUser
            )
            addChangeHistory.execute(
                projectId = projectId,
                taskId = UUID.randomUUID(),
                authorId = currentUser.userId,
                changeDate = Date(Date().time) ,
                changeDescription = "Project created",
            )

            ui.displayMessage("✅ Project '${name}' created successfully! 🎉")
            defaultProjectStateUseCase.initializeProjectState(
                projectId = projectId
            )
            handleTaskCreation(
                ui = ui,
                inputReader = inputReader,
                projectId = projectId
            )
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to create project"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }

    private fun collectProjectInfo(ui: UiDisplayer, inputReader: InputReader): Pair<String, String> {
        ui.displayMessage("🔹 Enter project name:")
        val projectName = inputReader.readString("Project Name: ").trim()
        if (projectName.isBlank()) {
            throw IllegalArgumentException("Project name must not be blank")
        }
        ui.displayMessage("🔹 Enter project description:")
        val projectDescription = inputReader.readString("Project Description: ").trim()
        if (projectDescription.isBlank()) {
            throw IllegalArgumentException("Project description must not be blank")
        }
        return projectName to projectDescription
    }

    private suspend fun createInitialState(ui: UiDisplayer, inputReader: InputReader): ProjectState {
        ui.displayMessage("🔹 Enter initial project state:")
        val stateName = inputReader.readString("State Name: ").trim()
        if (stateName.isBlank()) {
            throw IllegalArgumentException("State name must not be blank")
        }
        val project = ProjectState(projectId = UUID.randomUUID(), stateName = stateName)
        addProjectStatesUseCase.execute(
            stateName = stateName,
            projectId = project.projectId
        )
        return project
    }

    private fun buildProject(
        name: String,
        description: String,
        userId: UUID,
        state: ProjectState
    ): Project {
        return Project(
            projectId = UUID.randomUUID(),
            name = name,
            description = description,
            state = state
        )
    }

    private suspend fun handleTaskCreation(
        ui: UiDisplayer,
        inputReader: InputReader,
        projectId: UUID
    ) {
        ui.displayMessage("🔹 Do you want to create a task for this project? (y/n)")
        val createTask = inputReader.readString("Create Task (y/n): ").trim().lowercase()
        if (createTask == "y") {
            while (true) {
                ui.displayMessage("🔹 Enter task title:")
                val taskTitle = inputReader.readString("Task Title: ").trim()
                if (taskTitle.isBlank()) {
                    ui.displayMessage("❌ Task title must not be blank")
                    continue
                }
                ui.displayMessage("🔹 Enter task description:")
                val taskDescription = inputReader.readString("Task Description: ").trim()
                if (taskDescription.isBlank()) {
                    ui.displayMessage("❌ Task description must not be blank")
                    continue
                }
                val taskState = createTaskState(ui, inputReader)
                val task = Task(
                    taskId = UUID.randomUUID(),
                    title = taskTitle,
                    description = taskDescription,
                    projectId = projectId,
                    state = taskState,
                    createdBy = UUID.randomUUID(),
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                createTaskUseCase.createTask(task)
                ui.displayMessage("✅ Task '${taskTitle}' created successfully! 🎉")
                ui.displayMessage("🔹 Do you want to create another task? (y/n)")
                val createAnotherTask = inputReader.readString("Create Another Task (y/n): ").trim().lowercase()
                if (createAnotherTask != "y") {
                    break
                }
            }
        }
    }

    private suspend fun createTaskState(
        ui: UiDisplayer,
        inputReader: InputReader
    ): ProjectState {
        ui.displayMessage("🔹 Enter task state:")
        val taskStateName = inputReader.readString("Task State Name: ").trim()
        if (taskStateName.isBlank()) {
            throw IllegalArgumentException("Task state name must not be blank")
        }
        val taskState = ProjectState(projectId = UUID.randomUUID(), stateName = taskStateName)
        addProjectStatesUseCase.execute(
            stateName = taskStateName,
            projectId = taskState.projectId
        )
        return taskState
    }
}