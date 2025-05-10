package org.example.presentation

import org.example.presentation.auth.LoginManagementUI
import org.example.presentation.auth.MainMenuUI
import org.example.presentation.auth.Session
import org.example.presentation.io.InputReader
import org.example.presentation.io.UiDisplayer
import org.example.presentation.menus.Menu
import org.example.presentation.menus.MenuAction

class CLIMenu(
    private val loginView: LoginManagementUI,
    private val userManagementView: MainMenuUI
) : MenuAction {
    override val description: String = """
        ╔════════════════════════════════════╗
        ║  🌟 PlanMate: Create New User 🌟   ║
        ║  🚀 Empower Your Task Journey! 🚀  ║
        ╚════════════════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(
        ui: UiDisplayer,
        inputReader: InputReader
    ) {
        while (Session.getUser() == null) {
            ui.displayMessage(description)
            loginView.execute(ui, inputReader)
        }
        userManagementView.execute(ui, inputReader)
    }
}