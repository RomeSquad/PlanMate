package org.example.presentation.user.admin

import org.example.presentation.history.ShowHistoryManagementUI
import org.example.presentation.project.ProjectManagementUI
import org.example.presentation.task.TaskManagementUI
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class AdminManagementUI(
    private val projectManagementUI: ProjectManagementUI,
    private val taskManagementUI: TaskManagementUI,
    private val createUserUi: CreateUserUi,
    private val deleteUserUi: DeleteUserUi,
    private val editUserUI: EditUserUI,
    private val viewAllUserUI: ViewAllUserUI,
    private val changeHistoryManagementUI: ShowHistoryManagementUI
) : MenuAction {
    override val description: String = buildMainDescription()
    override val menu: Menu = Menu()

    private val menuOptions = listOf(
        MenuOption(1, "Manage Projects", projectManagementUI::execute, "📂"),
        MenuOption(2, "Manage Users", ::showUserManagementMenu, "👥"),
        MenuOption(3, "Manage Tasks", taskManagementUI::execute, "✅"),
        MenuOption(4, "View Audit Logs", changeHistoryManagementUI::execute, "📜"),
        MenuOption(5, "Logout", { _, _ -> }, "🚪")
    )

    private val userMenuOptions = listOf(
        MenuOption(1, "Create User", createUserUi::execute, "➕"),
        MenuOption(2, "Delete User", deleteUserUi::execute, "🗑️"),
        MenuOption(3, "Edit User", editUserUI::execute, "✏️"),
        MenuOption(4, "View All Users", viewAllUserUI::execute, "📜"),
        MenuOption(5, "Back", { _, _ -> }, "⬅️")
    )

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runMenuLoop(ui, inputReader, menuOptions, description) { choice ->
            choice == menuOptions.last().id // Logout option
        }
    }

    private suspend fun showUserManagementMenu(ui: UiDisplayer, inputReader: InputReader) {
        runMenuLoop(ui, inputReader, userMenuOptions, buildUserMenuDescription()) { choice ->
            choice == userMenuOptions.last().id // Back option
        }
    }

    private suspend fun runMenuLoop(
        ui: UiDisplayer,
        inputReader: InputReader,
        options: List<MenuOption>,
        description: String,
        isExitOption: (Int) -> Boolean
    ) {
        while (true) {
            try {
                ui.displayMessage(description)
                displayMenuOptions(ui, options)
                val choice = readUserChoice(ui, inputReader, options)
                if (isExitOption(choice)) return
                executeMenuAction(ui, inputReader, options, choice)
            } catch (e: IllegalArgumentException) {
                ui.displayMessage("❌ Invalid input: ${e.message ?: "Please enter a valid number"}")
            } catch (e: Exception) {
                ui.displayMessage("❌ Error: ${e.message ?: "Failed to execute action"}")
            }
        }
    }

    private fun displayMenuOptions(ui: UiDisplayer, options: List<MenuOption>) {
        val menuText = options.joinToString("\n") { "${it.icon} ${it.id}. ${it.description}" } +
                "\n🛠️ Select an option (1-${options.size}):"
        ui.displayMessage(menuText)
    }

    private fun readUserChoice(ui: UiDisplayer, inputReader: InputReader, options: List<MenuOption>): Int {
        val choice = inputReader.readString("Choice: ").trim().toIntOrNull()
        if (choice == null || choice !in options.map { it.id }) {
            throw IllegalArgumentException("Please select a number between 1 and ${options.size}")
        }
        return choice
    }

    private suspend fun executeMenuAction(
        ui: UiDisplayer,
        inputReader: InputReader,
        options: List<MenuOption>,
        choice: Int
    ) {
        val action = options.find { it.id == choice }?.action
            ?: throw IllegalStateException("Invalid menu option selected")
        action(ui, inputReader)
    }

    private fun buildMainDescription(): String = """
        ╔════════════════════════╗
        ║  Admin Control Center  ║
        ╚════════════════════════╝
        """.trimIndent()

    private fun buildUserMenuDescription(): String = """
        ╔══════════════════════════╗
        ║     User Management      ║
        ╚══════════════════════════╝
        """.trimIndent()

    private data class MenuOption(
        val id: Int,
        val description: String,
        val action: suspend (UiDisplayer, InputReader) -> Unit,
        val icon: String
    )
}