package org.example.presentation.project

import org.example.logic.usecase.project.DeleteProjectByIdUseCase
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import presentation.io.InputReader


class DeleteProjectUi(
    private val deleteProjectUseCase: DeleteProjectByIdUseCase,
) : MenuAction {
    override val description: String = """
        ╔════════════════════════════╗
        ║    Delete a Project        ║
        ╚════════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Enter Project ID to delete:")
            val projectIdInput = inputReader.readString("Project ID: ").trim()
            if (projectIdInput.isBlank()) {
                throw IllegalArgumentException("Project ID must not be blank")
            }
            val projectId = projectIdInput.toIntOrNull()
                ?: throw IllegalArgumentException("Project ID must be a valid number")

            ui.displayMessage("⚠️ Are you sure you want to delete project '$projectId'? [y/n]")
            val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
            if (confirmation != "y") {
                ui.displayMessage("🛑 Project deletion canceled.")
                return
            }

            val result = deleteProjectUseCase.deleteProjectById(projectId)
            result.fold(
                onSuccess = {
                    ui.displayMessage("✅ Project '$projectId' deleted successfully!")
                },
                onFailure = { error ->
                    ui.displayMessage("❌ Failed to delete project '$projectId': ${error.message}")
                }
            )
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to delete project"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}