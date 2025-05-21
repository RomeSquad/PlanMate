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
import org.example.presentation.utils.menus.BaseMenuAction
import java.util.*

class EditProjectUI(
    private val editProjectUseCase: EditProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val addChangeHistory: AddChangeHistoryUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : BaseMenuAction() {

    override val title: String = "Edit a Project"

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
            )
            if (selectedProject == null) {
                ui.displayMessage("❌ No projects available for editing.")
                return@executeWithErrorHandling
            }
            val updatedProject = collectProjectUpdates(ui, inputReader, selectedProject)
            saveProjectUpdates(updatedProject)
            logProjectEdit(updatedProject.projectId, currentUser)
            ui.displayMessage("✅ Project '${updatedProject.name}' updated successfully!")
        }
    }

    private fun collectProjectUpdates(ui: UiDisplayer, inputReader: InputReader, project: Project): Project {
        val name = readValidatedInput(
            ui, inputReader, "🔹 Enter new project name:", "Project Name", "Project name must not be blank",
            { it.takeIf { it.isNotBlank() } ?: project.name }, hint = "leave empty to keep '${project.name}'"
        )
        val description = readValidatedInput(
            ui, inputReader, "🔹 Enter new description:", "Description", "Description must not be blank",
            { it.takeIf { it.isNotBlank() } ?: project.description }, hint = "leave empty to keep current"
        )
        val state = readValidatedInput(
            ui,
            inputReader,
            "🔹 Enter new project state:",
            "Project State",
            "State must not be blank",
            { it.takeIf { it.isNotBlank() } ?: project.state.stateName },
            hint = "leave empty to keep '${project.state.stateName}'"
        )
        return Project(
            projectId = project.projectId,
            name = name,
            description = description,
            state = ProjectState(projectId = project.projectId, stateName = state)
        )
    }

    private suspend fun saveProjectUpdates(project: Project) {
        editProjectUseCase.execute(project)
    }

    private suspend fun logProjectEdit(projectId: UUID, user: User) {
        addChangeHistory.execute(
            projectId = projectId,
            taskId = null,
            authorId = user.userId,
            changeDate = Date(),
            changeDescription = "Project edited"
        )
    }
}