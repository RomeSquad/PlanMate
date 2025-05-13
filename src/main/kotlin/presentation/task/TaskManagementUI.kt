package org.example.presentation.task

import org.example.presentation.history.ShowHistoryManagementUI
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class TaskManagementUI(
    private val createTaskUi: CreateTaskUI,
    private val deleteTaskUi: DeleteTaskUI,
    private val editTaskUi: EditTaskUI,
    private val getAllTasksUi: GetAllTasksUI,
    private val changeHistoryManagementUI: ShowHistoryManagementUI
) : MenuAction {
    private val options = listOf(
        "➕ 1. Create Task",
        "🗑️ 2. Delete Task",
        "✏️ 3. Edit Task",
        "📜 4. Show Task History",
        "📜 5. List All Tasks",
        "⬅️ 6. Back to Main Menu"
    )

    override val description: String = """
        ╔══════════════════════════╗
        ║   Task Management Menu   ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        while (true) {
            ui.displayMessage(description)
            ui.displayMessage(options.joinToString("\n"))
            ui.displayMessage("🔹 Choose an option (1-7):")
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {
                1 -> createTaskUi.execute(ui, inputReader)
                2 -> deleteTaskUi.execute(ui, inputReader)
                3 -> editTaskUi.execute(ui, inputReader)
                4 -> changeHistoryManagementUI.execute(ui, inputReader)
                5 -> getAllTasksUi.execute(ui, inputReader)
                6 -> return
                else -> ui.displayMessage("❌ Invalid option. Please select a number between 1 and 6.")
            }
        }
    }
}