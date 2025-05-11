package org.example.presentation.project

import org.example.logic.usecase.project.DeleteProjectByIdUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class DeleteProjectUi(
    private val deleteProjectUseCase: DeleteProjectByIdUseCase
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
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }

            deleteProjectUseCase.deleteProjectById(projectId)
            ui.displayMessage("✅ Project '$projectId' deleted successfully!")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to delete project"}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}