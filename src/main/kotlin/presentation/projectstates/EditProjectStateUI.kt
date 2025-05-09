package org.example.presentation.projectstates


import org.example.logic.usecase.state.EditProjectStateUseCase
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import presentation.io.InputReader

class EditProjectStateUI(
    private val editProjectStateUseCase: EditProjectStateUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║   Edit Project State     ║
        ╚══════════════════════════╝
    """.trimIndent()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        ui.displayMessage(description)

        val stateId = promptForStateId(ui, inputReader) ?: return
        val newStateName = promptForNewStateName(ui, inputReader) ?: return

        if (!confirmStateChange(ui, inputReader, stateId, newStateName)) {
            ui.displayMessage("🛑 State edit canceled.")
            return
        }

        try {
            editProjectStateUseCase.execute(stateId, newStateName)
            ui.displayMessage("✅ State '$stateId' updated to '$newStateName' successfully!")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message}")
        }
    }

    private fun promptForStateId(ui: UiDisplayer, inputReader: InputReader): Int? {
        ui.displayMessage("🔹 Enter State ID:")
        val stateIdInput = inputReader.readString("State ID: ").trim()
        return try {
            stateIdInput.toInt()
        } catch (e: NumberFormatException) {
            ui.displayMessage("❌ Invalid State ID. Please enter a valid number ${e.message}.")
            null
        }
    }

    private fun promptForNewStateName(ui: UiDisplayer, inputReader: InputReader): String? {
        ui.displayMessage("🔹 Enter New State Name:")
        val newStateName = inputReader.readString("New State Name: ").trim()
        return newStateName.ifBlank {
            ui.displayMessage("❌ New state name cannot be blank.")
            null
        }
    }

    private fun confirmStateChange(
        ui: UiDisplayer,
        inputReader: InputReader,
        stateId: Int,
        newStateName: String
    ): Boolean {
        ui.displayMessage("⚠️ Change state '$stateId' to '$newStateName'? [y/n]")
        val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
        return confirmation == "y"
    }
}