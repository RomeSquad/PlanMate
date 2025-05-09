package org.example.presentation

import org.example.presentation.auithentication.LoginManagementUI
import org.example.presentation.auithentication.MainMenuUI
import org.example.presentation.auithentication.Session
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import presentation.io.InputReader

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