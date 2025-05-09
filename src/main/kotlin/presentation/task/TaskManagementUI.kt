package org.example.presentation.task

import org.example.presentation.action.history.ShowTaskHistoryMenuAction
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import presentation.io.InputReader

class TaskManagementUI(
    private val createTaskUi: CreateTaskUI,
    private val deleteTaskUi: DeleteTaskUI,
    private val editTaskUi: EditTaskUI,
    private val getTaskByIdUi: GetTaskByIdUI,
    private val showTaskHistoryMenuAction: ShowTaskHistoryMenuAction,
    private val getTasksByProjectIdUi: GetTasksByProjectIdUI,
    private val getAllTasksUi: GetAllTasksUI,
) : MenuAction {
    private val options = listOf(
        "➕ 1. Create Task",
        "🗑️ 2. Delete Task",
        "✏️ 3. Edit Task",
        "📜 4. Get Task by ID",
        "📜 5. List Tasks by Project ID",
        "📜 6. Show Task History",
        "📜 7. List All Tasks",
        "⬅️ 8. Back to Main Menu"
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
                4 -> getTaskByIdUi.execute(ui, inputReader)
                5 -> getTasksByProjectIdUi.execute(ui, inputReader)
                6 -> showTaskHistoryMenuAction.execute(ui, inputReader)
                7 -> getAllTasksUi.execute(ui, inputReader)
                8 -> return
                else -> ui.displayMessage("❌ Invalid option. Please select a number between 1 and 7.")
            }
        }
    }
}