package org.example.presentation.project

import logic.usecase.project.EditProjectUseCase
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.entity.auth.User
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.history.AddChangeHistoryUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.util.*

class EditProjectUi(
    private val editProjectUseCase: EditProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val addChangeHistory: AddChangeHistoryUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : MenuAction {

    override val description: String = """
        ╔════════════════════════════╗
        ║       Edit a Project       ║
        ╚════════════════════════════╝
    """.trimIndent()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runCatching {
            ui.displayMessage(description)
            val projects = fetchProjects(ui)
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available for editing.")
                return@runCatching
            }
            displayProjects(ui, projects)
            val selectedProject = selectProject(ui, inputReader, projects) ?: return@runCatching
            val updatedProject = collectProjectUpdates(ui, inputReader, selectedProject)
            saveProjectUpdates(updatedProject)
            logProjectEdit(updatedProject.projectId, getCurrentUser())
            ui.displayMessage("✅ Project '${updatedProject.name}' updated successfully!")
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

    private fun displayProjects(ui: UiDisplayer, projects: List<Project>) {
        ui.displayMessage("📂 Available Projects:")
        projects.forEachIndexed { index, project ->
            ui.displayMessage("📌 ${index + 1}. ${project.name} | 🆔 ID: ${project.projectId}")
        }
        ui.displayMessage("🔹 Please enter a number to choose a project.")
    }

    private fun selectProject(ui: UiDisplayer, inputReader: InputReader, projects: List<Project>): Project? {
        val projectIndex = inputReader.readIntOrNull(
            string = "🔹 Select a project to edit (1-${projects.size}): ",
            ints = 1..projects.size
        )?.minus(1)
        if (projectIndex == null || projectIndex !in projects.indices) {
            ui.displayMessage("❌ Invalid selection. Please try again.")
            return null
        }
        val selectedProject = projects[projectIndex]
        ui.displayMessage("🔹 You selected: ${selectedProject.name} | 🆔 ID: ${selectedProject.projectId}")
        return selectedProject
    }

    private data class ProjectUpdates(val name: String, val description: String, val state: String)

    private fun collectProjectUpdates(ui: UiDisplayer, inputReader: InputReader, project: Project): Project {
        val updates = ProjectUpdates(
            name = readNonBlankInput(
                ui,
                inputReader,
                "🔹 Enter new project name:",
                "Project Name",
                "Project name must not be blank"
            ),
            description = readNonBlankInput(
                ui,
                inputReader,
                "🔹 Enter new description:",
                "Description",
                "Project description must not be blank"
            ),
            state = readNonBlankInput(
                ui,
                inputReader,
                "🔹 Enter new project state:",
                "Project State",
                "Project state must not be blank"
            )
        )
        return Project(
            projectId = project.projectId,
            name = updates.name,
            description = updates.description,
            state = ProjectState(projectId = project.projectId, stateName = updates.state)
        )
    }

    private suspend fun saveProjectUpdates(project: Project) {
        editProjectUseCase.execute(project)
    }

    private suspend fun getCurrentUser(): User {
        return getCurrentUserUseCase.getCurrentUser()
            ?: throw IllegalStateException("No authenticated user found. Please log in.")
    }

    private suspend fun logProjectEdit(projectId: UUID, user: User) {
        addChangeHistory.execute(
            projectId = projectId,
            taskId = UUID.randomUUID(),
            authorId = user.userId,
            changeDate = Date(),
            changeDescription = "Project edited"
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
            else -> "❌ An unexpected error occurred: ${exception.message ?: "Failed to update project"}"
        }
        ui.displayMessage(message)
    }
}