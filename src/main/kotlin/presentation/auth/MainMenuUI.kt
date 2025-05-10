package org.example.presentation.auth

import org.example.logic.entity.auth.UserRole
import org.example.presentation.io.InputReader
import org.example.presentation.io.UiDisplayer
import org.example.presentation.menus.Menu
import org.example.presentation.menus.MenuAction
import org.example.presentation.user.admin.AdminManagementUI
import org.example.presentation.user.mate.MateManagementUI


class MainMenuUI(
    private val adminManagementUI: AdminManagementUI,
    private val mateManagementUI: MateManagementUI,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║      Main Menu           ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        while (true) {
            ui.displayMessage(description)
            val options = if (
                Session.currentUser?.userRole == UserRole.ADMIN
            ) mutableListOf(
                "👑 1. Admin Management",
                "📋 2. Mate Management",
                "🚪 3. Logout"
            ) else mutableListOf(
                "📋 1. Mate Management",
                "🚪 2. Logout"
            )
            ui.displayMessage(options.joinToString("\n"))
            ui.displayMessage("🔹 Choose an option (1-${options.size}):")
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {
                1 -> if (Session.currentUser?.userRole == UserRole.ADMIN) {
                    adminManagementUI.execute(ui, inputReader)
                } else {
                    mateManagementUI.execute(ui, inputReader)
                }

                2 -> if (Session.currentUser?.userRole == UserRole.ADMIN) {
                    mateManagementUI.execute(ui, inputReader)
                } else {
                    ui.displayMessage("🔙 Logging out...")
                    Session.currentUser = null
                    return
                }

                3 -> if (Session.currentUser?.userRole == UserRole.ADMIN) {
                    ui.displayMessage("🔙 Logging out...")
                    Session.currentUser = null
                    return
                } else {
                    ui.displayMessage("❌ Invalid option.")
                }

                else -> ui.displayMessage("❌ Invalid option.")
            }
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}