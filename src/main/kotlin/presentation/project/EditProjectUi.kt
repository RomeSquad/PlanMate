package org.example.presentation.project

import logic.usecase.project.EditProjectUseCase
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.util.UUID


class EditProjectUi(
    private val editProjectUseCase: EditProjectUseCase
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
            val updatedProject = collectProjectInput(ui, inputReader)

            editProjectUseCase.execute(updatedProject)
            ui.displayMessage("✅ Project '${updatedProject.projectId}' updated successfully!")

        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to update project"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }

    private fun collectProjectInput(ui: UiDisplayer, inputReader: InputReader): Project {
        ui.displayMessage("🔹 Enter project ID to edit:")
        val idInput = inputReader.readString("Project ID: ").trim()
        val id = UUID.fromString(idInput)

        ui.displayMessage("🔹 Enter new project name:")
        val name = inputReader.readString("Project Name: ").trim()
        if (name.isBlank()) {
            throw IllegalArgumentException("Project name must not be blank")
        }

        ui.displayMessage("🔹 Enter new description:")
        val description = inputReader.readString("Description: ").trim()

        return Project(
            projectId = id,
            name = name,
            description = description,
            state = ProjectState(projectId = id, stateName = "In progress")
        )
    }
}