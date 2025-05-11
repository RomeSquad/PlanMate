package org.example.presentation.projectstates

import org.example.logic.usecase.state.DeleteProjectStatesUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.util.UUID

class DeleteStateToProjectUI(
    private val deleteProjectStatesUseCase: DeleteProjectStatesUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║ Delete State from Project║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Enter State ID:")
            val stateIdInput = inputReader.readString("State ID: ").trim()
            if (stateIdInput.isBlank()) {
                throw IllegalArgumentException("State ID must not be blank")
            }
            val stateId = UUID.fromString(stateIdInput)

            ui.displayMessage("⚠️ Delete state with ID '$stateId'? [y/n]")
            val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
            if (confirmation != "y") {
                ui.displayMessage("🛑 State deletion canceled.")
                return
            }

            deleteProjectStatesUseCase.execute(stateId)
            ui.displayMessage("✅ State with ID '$stateId' deleted successfully.")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to delete state"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}