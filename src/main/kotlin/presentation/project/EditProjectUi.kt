package org.example.presentation.project

import logic.usecase.project.EditProjectUseCase
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.history.AddChangeHistoryUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.util.Date
import java.util.UUID


class EditProjectUi(
    private val editProjectUseCase: EditProjectUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val addChangeHistory: AddChangeHistoryUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : MenuAction {
    override val description: String = """
        ╔════════════════════════════╗
        ║       Edit a Project       ║
        ╚════════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Fetching all projects...")
            val projects = getAllProjectsUseCase.getAllProjects()
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available for editing.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("📂 Available Projects:")
            projects.forEachIndexed { index, project ->
                ui.displayMessage("📌 ${index + 1}. ${project.name} | 🆔 ID: ${project.projectId}")
            }
            val projectIndex =
                inputReader.readIntOrNull("🔹 Select a project to edit (1-${projects.size}): ", 1..projects.size)
                    ?.minus(1)
            if (projectIndex == null || projectIndex < 0 || projectIndex >= projects.size) {
                ui.displayMessage("❌ Invalid selection. Please try again.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            val selectedProject = projects[projectIndex]
            ui.displayMessage("🔹 You selected: ${selectedProject.name} | 🆔 ID: ${selectedProject.projectId}")
            ui.displayMessage("🔹 Enter new project name:")
            val name = inputReader.readString("Project Name: ").trim()
            if (name.isBlank()) {
                throw IllegalArgumentException("Project name must not be blank")
            }
            ui.displayMessage("🔹 Enter new description:")
            val description = inputReader.readString("Description: ").trim()
            if (description.isBlank()) {
                throw IllegalArgumentException("Project description must not be blank")
            }
            ui.displayMessage("🔹 Enter new project state:")
            val state = inputReader.readString("Project State: ").trim()
            if (state.isBlank()) {
                throw IllegalArgumentException("Project state must not be blank")
            }
            val updatedProject = Project(
                projectId = selectedProject.projectId,
                name = name,
                description = description,
                state = ProjectState(projectId = selectedProject.projectId, stateName = state)
            )
            editProjectUseCase.execute(updatedProject)
            val currentUser = getCurrentUserUseCase.getCurrentUser()
            addChangeHistory.execute(
                projectId = selectedProject.projectId,
                taskId = UUID.fromString("0"),
                authorId = currentUser!!.userId,
                changeDate = Date(Date().time) ,
                changeDescription = "Project edited",
            )
            ui.displayMessage("✅ Project '${updatedProject.name}' updated successfully!")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to update project"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }

}