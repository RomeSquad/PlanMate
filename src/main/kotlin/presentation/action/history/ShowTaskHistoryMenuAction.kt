package org.example.presentation.action.history

import org.example.logic.usecase.history.ShowTaskHistoryUseCase
import org.example.presentation.formatter.CliFormatter
import org.example.presentation.formatter.dataFormatter.format
import org.example.presentation.menus.Menu
import org.example.presentation.menus.MenuAction
import presentation.io.InputReader
import presentation.io.UiDisplayer

class ShowTaskHistoryMenuAction(
    private val showTaskHistoryUseCase: ShowTaskHistoryUseCase
) : MenuAction {

    override val description: String = """
        ╔════════════════════════════════════╗
        ║        Get Task History by ID      ║
        ╚════════════════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Enter Project ID:")
            val idInput = inputReader.readString().trim()
            if (idInput.isBlank()) {
                throw IllegalArgumentException("Project ID must not be blank")
            }
            val id = idInput.toIntOrNull()
                ?: throw IllegalArgumentException("Project ID must be a valid number")

            val result = showTaskHistoryUseCase.execute(id)
            if (result.isEmpty()) {
                ui.displayMessage("❌ No history found for project ID: $id")
                return
            }
            val formatter = CliFormatter()
            val show = formatter.verticalLayout(result.map { it.format() })
            ui.displayMessage("✅ Change History Details:")
            ui.displayMessage(show)
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to retrieve project"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString()
        }
    }
}