package org.example.presentation.projectstates

import org.example.logic.usecase.state.GetAllProjectStatesUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class GetAllStatesPerProjectUI(
    private val getAllProjectStatesUseCase: GetAllProjectStatesUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║ States per Project Menu  ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage("🔹 Enter Project ID:")
            val projectId = inputReader.readString("Project ID: ").trim()
            if (projectId.isBlank()) {
                throw IllegalArgumentException("Project ID must not be blank")
            }

            val allStates = getAllProjectStatesUseCase.execute()
            val projectStates = allStates.filter {
                it.projectId == projectId.toInt()
            }

            if (projectStates.isEmpty()) {
                ui.displayMessage("ℹ️ No states found for project ID '$projectId'.")
            } else {
                ui.displayMessage("✅ States for Project ID '$projectId':")
                projectStates.forEachIndexed { index, state ->
                    ui.displayMessage(
                        """
                        State ${index + 1}:
                        Name: ${state.stateName}
                        Project ID: ${state.projectId}
                        --------------------
                    """.trimIndent()
                    )
                }
            }

            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message}")
        }
    }
}