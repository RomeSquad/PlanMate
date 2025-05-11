package org.example.presentation.project

import org.example.logic.entity.CreateProjectRequest
import org.example.logic.usecase.project.InsertProjectUseCase
import org.example.logic.usecase.state.DefaultProjectStateUseCase
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import org.example.presentation.utils.io.InputReader


class CreateProjectUi(
    private val insertProjectUseCase: InsertProjectUseCase,
    private val defaultProjectStateUseCase: DefaultProjectStateUseCase
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

            val projectRequest = collectProjectInput(ui, inputReader)
            val projectResponse = insertProjectUseCase.insertProject(projectRequest)

            ui.displayMessage("✅ Project created successfully: ${projectResponse.id}")
            defaultProjectStateUseCase.initializeProjectState(projectResponse.id)

        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to create project"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }

    private fun collectProjectInput(ui: UiDisplayer, inputReader: InputReader): CreateProjectRequest {
        ui.displayMessage("🔹 Enter project name:")
        val projectName = inputReader.readString("Project Name: ").trim()

            ui.displayMessage("🔹 Enter user ID (numeric):")
            val userId = inputReader.readString("User ID: ").trim().toIntOrNull()
                ?: throw IllegalArgumentException("User ID must be a valid number")

        ui.displayMessage("🔹 Enter username:")
        val userName = inputReader.readString("Username: ").trim()
        if (userName.isBlank()) {
            throw IllegalArgumentException("Username must not be blank")
        }

        ui.displayMessage("🔹 Enter project description:")
        val description = inputReader.readString("Description: ").trim()

        return CreateProjectRequest(
            name = projectName,
            userId = userId,
            userName = userName,
            description = description
        )
    }
}