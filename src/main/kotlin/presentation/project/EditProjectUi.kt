package org.example.presentation.project

import logic.usecase.project.EditProjectUseCase
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import presentation.io.InputReader


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
            ui.displayMessage("🔹 Enter project ID to edit:")
            val idInput = inputReader.readString("Project ID: ").trim()
            val id = idInput.toIntOrNull()
                ?: throw IllegalArgumentException("Project ID must be a valid number")

            ui.displayMessage("🔹 Enter new project name:")
            val name = inputReader.readString("Project Name: ").trim()
            if (name.isBlank()) {
                throw IllegalArgumentException("Project name must not be blank")
            }

            ui.displayMessage("🔹 Enter new description:")
            val description = inputReader.readString("Description: ").trim()

            val updatedProject = Project(
                id = id,
                name = name,
                description = description,
                state = ProjectState(projectId = id, stateName = "In progress")
            )

            val result = editProjectUseCase.execute(updatedProject)
            result.fold(
                onSuccess = {
                    ui.displayMessage("✅ Project '$id' updated successfully!")
                },
                onFailure = { error ->
                    ui.displayMessage("❌ Failed to update project '$id': ${error.message}")
                }
            )
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