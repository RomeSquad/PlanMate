package org.example.presentation.project

import org.example.logic.entity.Project
import org.example.logic.usecase.project.GetProjectByIdUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class GetProjectByIdUI(
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) : MenuAction {
    override val description: String = """
        ╔════════════════════════════╗
        ║      Get Project by ID     ║
        ╚════════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            val projectId = collectProjectIdInput(ui, inputReader)

            val project = getProjectByIdUseCase.getProjectById(projectId)
            displayProjectDetails(ui, project)

        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to retrieve project"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }

    private fun collectProjectIdInput(ui: UiDisplayer, inputReader: InputReader): Int {
        ui.displayMessage("🔹 Enter Project ID:")
        val idInput = inputReader.readString("Project ID: ").trim()
        if (idInput.isBlank()) {
            throw IllegalArgumentException("Project ID must not be blank")
        }
        return idInput.toIntOrNull()
            ?: throw IllegalArgumentException("Project ID must be a valid number")
    }

    private fun displayProjectDetails(ui: UiDisplayer, project: Project) {
        ui.displayMessage("✅ Project Details:")
        ui.displayMessage(
            """
            ╔══════════════════════════╗
            ║ Project ID: ${project.id} ║
            ║ Project Name: ${project.name} ║
            ║ Project Description: ${project.description} ║
            ║ Project State: ${project.state.stateName} ║
            ╚══════════════════════════╝
            """.trimIndent()
        )
    }
}