package org.example.presentation.projectstates

import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class ProjectStateManagementUI(
    private val addStateToProjectUI: AddStateToProjectUI,
    private val addTaskStateToProjectUI: AddTaskStateToProjectUI,
    private val editProjectStateUI: EditProjectStateUI,
    private val deleteStateToProjectUI: DeleteStateToProjectUI,
    private val getAllStatesPerProjectUI: GetAllStatesPerProjectUI,
) : MenuAction {
    private val options = listOf(
        "➕ 1. Add Task State to Project",
        "➕ 2. Add State to Project",
        "✏️ 3. Edit Project State",
        "🗑️ 4. Delete Project State",
        "📜 5. List All States for Project",
        "⬅️ 6. Back to Project Management"
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
            ui.displayMessage("🔹 Choose an option (1-6):")
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {
                1 -> addTaskStateToProjectUI.execute(ui, inputReader)
                2 -> addStateToProjectUI.execute(ui, inputReader)
                3 -> editProjectStateUI.execute(ui, inputReader)
                4 -> deleteStateToProjectUI.execute(ui, inputReader)
                5 -> getAllStatesPerProjectUI.execute(ui, inputReader)
                6 -> return
                else -> {
                    ui.displayMessage("❌ Invalid option. Please select a number between 1 and 5.")
                    ui.displayMessage("🔄 Press Enter to continue...")
                    inputReader.readString("")
                }
            }
        }
    }
}