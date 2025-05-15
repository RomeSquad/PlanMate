package org.example.presentation.user.mate

import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.presentation.task.TaskManagementUI
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class MateManagementUI(
    private val taskManagementUI: TaskManagementUI,
    private val getCurrentUser: GetCurrentUserUseCase
) : MenuAction {

    override val description: String = """
        ╔══════════════════════════╗
        ║    Mate Control Center   ║
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
                📋 1. Manage Tasks
                ⬅️ 2. Back
                """.trimIndent()
            )
            ui.displayMessage("🔹 Select an option (1-2):")
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {
                1 -> taskManagementUI.execute(ui, inputReader)
                2 -> {
                    ui.displayMessage("🔙 Returning to previous menu...")
                    return
                }
                else -> ui.displayMessage("❌ Invalid option. Please select a number between 1 and 2.")
            }

            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}