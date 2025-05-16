package org.example.presentation.history

import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class ShowHistoryManagementUI(
    private val showProjectHistoryUI: ShowProjectHistoryUI,
    private val showTaskHistoryUI: ShowTaskHistoryUI,
) : MenuAction {
    override val description: String = """
        ╔════════════════════════════════════╗
        ║      History Management Menu       ║
        ╚════════════════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    private val options = listOf(
        "📜 1. Show Project History",
        "📝 2. Show Task History",
        "⬅️ 3. Back to Main Menu"
    )

    override suspend fun execute(
        ui: UiDisplayer,
        inputReader: InputReader
    ) {
        while (true) {
            ui.displayMessage(description)
            ui.displayMessage(options.joinToString("\n"))
            ui.displayMessage("🔹 Choose an option (1-3):")
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {
                1 -> showProjectHistoryUI.execute(ui, inputReader)
                2 -> showTaskHistoryUI.execute(ui, inputReader)
                3 -> {
                    ui.displayMessage("✅ Exiting history management menu. Have a great day! 👋")
                    return
                }

                else -> ui.displayMessage("❌ Invalid choice. Please try again.")
            }
        }
    }
}
