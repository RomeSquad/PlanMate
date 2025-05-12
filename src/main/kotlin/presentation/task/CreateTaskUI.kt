package org.example.presentation.task

import logic.usecase.project.EditProjectUseCase
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
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
    private val addProjectStatesUseCase: AddProjectStatesUseCase
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║    Create a New Task     ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    lateinit var projects: List<Project>
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Enter Task Title:")
            val title = inputReader.readString("Title: ").trim()
            ui.displayMessage("🔹 Enter Task Description:")
            val description = inputReader.readString("Description: ").trim()
            projects = getAllProjectsUseCase.getAllProjects()
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available. Please create a project first.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("🔹 Select a Project:")
            projects.forEachIndexed { index, project ->
                ui.displayMessage("📂 ${index + 1}. ${project.name} (ID: ${project.projectId})")
            }
            val projectIndex =
                inputReader.readIntOrNull(
                    "Select the number of project that you want to add task to: ",
                    1..projects.size
                )
                    ?.minus(1)
                    ?: throw IllegalArgumentException("Invalid project selection.")
            val selectedProject = projects[projectIndex]
            val taskState = ProjectState(
                projectId = selectedProject.projectId,
                stateName = "TODO"
            )
            addProjectStatesUseCase.execute(taskState.stateName, taskState.projectId)
            val task = Task(
                taskId = UUID.randomUUID(),
                title = title,
                description = description,
                state = taskState,
                projectId = selectedProject.projectId,
                createdBy = UUID.randomUUID(),
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            ui.displayMessage("⚠️ Create task '$title' for project '${selectedProject.name}'? [y/n]")
            val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
            if (confirmation != "y") {
                ui.displayMessage("🛑 Task creation canceled.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            createTaskUseCase.createTask(task)
            val updatedProject = selectedProject.copy(
                state = taskState.copy(
                    projectId = selectedProject.projectId,
                    stateName = taskState.stateName
                ),
            )
            editProjectUseCase.execute(updatedProject)
            ui.displayMessage("✅ Task '$title' created successfully!")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to create task"}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (ex: Exception) {
            ui.displayMessage("❌ Failed to get all projects: ${ex.message}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } finally {
            ui.displayMessage("🔄 Press Enter to return to the main menu...")
            inputReader.readString("")
        }
    }
}