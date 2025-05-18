package org.example.presentation.task

import org.example.presentation.history.ShowHistoryManagementUI
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class TaskManagementUI(
    createTaskUi: CreateTaskUI,
    deleteTaskUi: DeleteTaskUI,
    editTaskUi: EditTaskUI,
    swimlanesView: SwimlanesView,
    changeHistoryManagementUI: ShowHistoryManagementUI
) : MenuAction {

    override val description: String = """
        ╔══════════════════════════╗
        ║   Task Management Menu   ║
        ╚══════════════════════════╝
    """.trimIndent()

    override val menu: Menu = Menu()

    private val menuOptions = listOf(
        MenuOption(1, "➕ Create Task", createTaskUi),
        MenuOption(2, "🗑️ Delete Task", deleteTaskUi),
        MenuOption(3, "✏️ Edit Task", editTaskUi),
        MenuOption(4, "📜 Show Task History", changeHistoryManagementUI),
        MenuOption(5, "📜 View Project Tasks", swimlanesView),
        MenuOption(6, "⬅️ Back to Main Menu", null)
    )

    private data class MenuOption(val number: Int, val description: String, val action: MenuAction?)

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        while (true) {
            runCatching {
                displayMenu(ui)
                val choice = selectOption(ui, inputReader)
                handleOption(choice, ui, inputReader)
                if (choice == 6) return // Exit loop for "Back to Main Menu"
            }.onFailure { exception ->
                handleError(ui, exception)
            }
        }
    }

    private fun displayMenu(ui: UiDisplayer) {
        ui.displayMessage(description)
        menuOptions.forEach { option ->
            ui.displayMessage("${option.number}. ${option.description}")
        }
    }

    private fun selectOption(ui: UiDisplayer, inputReader: InputReader): Int {
        ui.displayMessage("🔹 Please enter a number to choose an option (1-${menuOptions.size}): ")
        return inputReader.readIntOrNull(
            string = "",
            ints = 1..menuOptions.size
        ) ?: throw IllegalArgumentException("Invalid option. Please select a number between 1 and ${menuOptions.size}.")
    }

    private suspend fun handleOption(choice: Int, ui: UiDisplayer, inputReader: InputReader) {
        val option = menuOptions.find { it.number == choice }
        option?.action?.execute(ui, inputReader)
    }

    private fun handleError(ui: UiDisplayer, exception: Throwable) {
        val message = when (exception) {
            is IllegalArgumentException -> "❌ Error: ${exception.message}"
            else -> "❌ An unexpected error occurred: ${exception.message ?: "Failed to process option"}"
        }
        ui.displayMessage(message)
    }
}