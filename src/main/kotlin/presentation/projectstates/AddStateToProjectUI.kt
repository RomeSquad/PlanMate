package org.example.presentation.projectstates

import org.example.logic.usecase.state.AddProjectStatesUseCase
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import presentation.io.InputReader


class AddStateToProjectUI(
    private val addProjectStatesUseCase: AddProjectStatesUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║  Add State to Project    ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Enter State Name:")
            val stateName = inputReader.readString("State Name: ").trim()
            if (stateName.isBlank()) {
                throw IllegalArgumentException("State Name must not be blank")
            }

            ui.displayMessage("🔹 Enter Project ID:")
            val projectIdInput = inputReader.readString("Project ID: ").trim()
            if (projectIdInput.isBlank()) {
                throw IllegalArgumentException("Project ID must not be blank")
            }
            val projectId = projectIdInput.toIntOrNull()
                ?: throw IllegalArgumentException("Project ID must be a valid number")

            ui.displayMessage("⚠️ Add state '$stateName' to project '$projectId'? [y/n]")
            val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
            if (confirmation != "y") {
                ui.displayMessage("🛑 State addition canceled.")
                return
            }

            addProjectStatesUseCase.execute(stateName, projectId)
            ui.displayMessage("✅ State '$stateName' added to project '$projectId' successfully.")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ Failed to add state to project: ${e.message ?: "Unexpected error"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}