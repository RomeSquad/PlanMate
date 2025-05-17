package org.example.presentation.task

import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.presentation.history.ShowHistoryManagementUI
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class TaskManagementUI(
    private val createTaskUi: CreateTaskUI,
    private val deleteTaskUi: DeleteTaskUI,
    private val editTaskUi: EditTaskUI,
    private val swimlanesView: SwimlanesView,
    private val changeHistoryManagementUI: ShowHistoryManagementUI,
    private val getCurrentUser: GetCurrentUserUseCase
) : MenuAction {

    override val description: String = """
        ╔══════════════════════════╗
        ║   Task Management Menu   ║
        ╚══════════════════════════╝
    """.trimIndent()

    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        val user = getCurrentUser.getCurrentUser()

        if (user == null) {
            ui.displayMessage("❌ No authenticated user found! Please log in first.")
            return
        }

        ui.displayMessage("👤 Welcome ${user.username} (${user.userRole})!")

        while (true) {
            ui.displayMessage(description)
            ui.displayMessage(
                """
                ➕ 1. Create Task
                🗑 2. Delete Task
                ✏️ 3. Edit Task
                📜 4. Show Task History
                📋 5. View Project Tasks
                ⬅️ 6. Back to Main Menu
                """.trimIndent()
            )
            ui.displayMessage("🔹 Choose an option (1-6):")
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {
                1 -> createTaskUi.execute(ui, inputReader)
                2 -> deleteTaskUi.execute(ui, inputReader)
                3 -> editTaskUi.execute(ui, inputReader)
                4 -> changeHistoryManagementUI.execute(ui, inputReader)
                5 -> swimlanesView.execute(ui, inputReader)
                6 -> {
                    ui.displayMessage("🔙 Returning to Main Menu...")
                    return
                }
                else -> ui.displayMessage("❌ Invalid option. Please select a number between 1 and 6.")
            }

            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}