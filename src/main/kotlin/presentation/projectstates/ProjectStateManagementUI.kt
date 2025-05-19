package org.example.presentation.projectstates

import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class ProjectStateManagementUI(
    addStateToProjectUI: AddStateToProjectUI,
    addTaskStateToProjectUI: AddTaskStateToProjectUI,
    editProjectStateUI: EditProjectStateUI,
    deleteStateToProjectUI: DeleteStateToProjectUI,
    getAllStatesPerProjectUI: GetAllStatesPerProjectUI
) : MenuAction {

    override val description: String = buildMenuDescription()

    override val menu: Menu = Menu()

    private val menuOptions = listOf(
        MenuOption(1, "Add Task State to Project", addTaskStateToProjectUI),
        MenuOption(2, "Add State to Project", addStateToProjectUI),
        MenuOption(3, "Edit Project State", editProjectStateUI),
        MenuOption(4, "Delete Project State", deleteStateToProjectUI),
        MenuOption(5, "List All States for Project", getAllStatesPerProjectUI),
        MenuOption(6, "Back to Project Management", null)
    )

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        while (true) {
            runCatching {
                displayMenu(ui)
                val choice = selectMenuOption(inputReader)
                handleMenuChoice(ui, inputReader, choice)
                if (choice == 6) return
            }.onFailure { exception ->
                handleError(ui, exception, inputReader)
            }
        }
    }

    private fun buildMenuDescription(): String = """
        ╔══════════════════════════╗
        ║ Project State Management ║
        ╚══════════════════════════╝
    """.trimIndent()

    private fun displayMenu(ui: UiDisplayer) {
        ui.displayMessage(description)
        menuOptions.forEach { option ->
            ui.displayMessage("➡️ ${option.number}. ${option.description}")
        }
        ui.displayMessage("🔹 Choose an option (1-${menuOptions.size}):")
    }

    private fun selectMenuOption(inputReader: InputReader): Int {
        val choice = inputReader.readString("Choice: ").trim().toIntOrNull()
        if (choice == null || choice < 1 || choice > menuOptions.size) {
            throw IllegalArgumentException("Invalid selection. Please select a number between 1 and ${menuOptions.size}.")
        }
        return choice
    }

    private suspend fun handleMenuChoice(ui: UiDisplayer, inputReader: InputReader, choice: Int) {
        val selectedOption = menuOptions.find { it.number == choice }
        selectedOption?.action?.execute(ui, inputReader)
    }

    private fun handleError(ui: UiDisplayer, exception: Throwable, inputReader: InputReader) {
        val message = when (exception) {
            is IllegalArgumentException -> "❌ Error: ${exception.message}"
            else -> "❌ An unexpected error occurred: ${exception.message ?: "Failed to process menu selection"}"
        }
        ui.displayMessage(message)
        ui.displayMessage("🔄 Press Enter to continue...")
        inputReader.readString("")
    }

    private data class MenuOption(
        val number: Int,
        val description: String,
        val action: MenuAction?
    )
}