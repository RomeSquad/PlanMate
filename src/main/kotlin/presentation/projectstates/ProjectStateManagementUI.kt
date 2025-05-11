package org.example.presentation.projectstates

import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import org.example.presentation.utils.io.InputReader

class ProjectStateManagementUI(
    private val addStateToProjectUI: AddStateToProjectUI,
    private val editProjectStateUI: EditProjectStateUI,
    private val deleteStateToProjectUI: DeleteStateToProjectUI,
    private val getAllStatesPerProjectUI: GetAllStatesPerProjectUI,
) : MenuAction {
    private val options = listOf(
        "➕ 1. Add State to Project",
        "✏️ 2. Edit Project State",
        "🗑️ 3. Delete Project State",
        "📜 4. List All States for Project",
        "⬅️ 5. Back to Project Management"
    )
    override val description: String = """
        ╔══════════════════════════╗
        ║ Project State Management ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        while (true) {
            ui.displayMessage(description)
            ui.displayMessage(options.joinToString("\n"))
            ui.displayMessage("🔹 Choose an option (1-5):")
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {
                1 -> addStateToProjectUI.execute(ui, inputReader)
                2 -> editProjectStateUI.execute(ui, inputReader)
                3 -> deleteStateToProjectUI.execute(ui, inputReader)
                4 -> getAllStatesPerProjectUI.execute(ui, inputReader)
                5 -> return
                else -> {
                    ui.displayMessage("❌ Invalid option. Please select a number between 1 and 5.")
                    ui.displayMessage("🔄 Press Enter to continue...")
                    inputReader.readString("")
                }
            }
        }
    }
}