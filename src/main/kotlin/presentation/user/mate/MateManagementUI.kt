package org.example.presentation.user.mate

import org.example.presentation.io.InputReader
import org.example.presentation.io.UiDisplayer
import org.example.presentation.menus.Menu
import org.example.presentation.menus.MenuAction

class MateManagementUI(
//    private val taskManagementUI: TaskManagementUI,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║    Mate Control Center   ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        while (true) {
            ui.displayMessage(description)
            ui.displayMessage(
                """
                📋 1. Manage Tasks
                ⬅️ 2. Back
                🔹 Select an option (1-2):
                """.trimIndent()
            )
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {
//                1 -> taskManagementUI.execute(ui, inputReader)
                2 -> return
                else -> ui.displayMessage("❌ Invalid option. Please select a number between 1 and 2.")
            }
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}