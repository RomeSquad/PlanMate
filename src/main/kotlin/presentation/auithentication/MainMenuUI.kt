package org.example.presentation.auithentication

import org.example.logic.entity.User.UserRole
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.presentation.user.admin.AdminManagementUI
import org.example.presentation.user.mate.MateManagementUI
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction


class MainMenuUI(
    private val adminManagementUI: AdminManagementUI,
    private val mateManagementUI: MateManagementUI,
    private val getCurrentUser: GetCurrentUserUseCase
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║        Main Menu         ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        while (true) {
            ui.displayMessage(description)
            val currentUser = getCurrentUser.getCurrentUser()
            if (currentUser == null) {
                ui.displayMessage("❌ User not logged in.")
                return
            }
            ui.displayMessage("🔹 Welcome, ${currentUser.username}!")
            ui.displayMessage("🔹 Your role: ${currentUser.userRole}")
            val options = if (
                currentUser.userRole == UserRole.ADMIN
            ) mutableListOf(
                "👑 1. Admin Management",
                "📋 2. Mate Management",
                "🚪 3. Logout"
            ) else mutableListOf(
                "📋 1. Mate Management",
                "🚪 2. Logout"
            )
            ui.displayMessage("🔹 Please choose an option:")
            ui.displayMessage(options.joinToString("\n"))
            ui.displayMessage("🔹 Choose an option (1-${options.size}):")
            ui.displayMessage("🔹 Enter your choice:")
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {

                1 -> if (
                    currentUser.userRole == UserRole.ADMIN
                ) {
                    adminManagementUI.execute(ui, inputReader)
                } else {
                    mateManagementUI.execute(ui, inputReader)
                }

                2 -> if (currentUser.userRole == UserRole.ADMIN) {
                    mateManagementUI.execute(ui, inputReader)
                } else {
                    ui.displayMessage("🔙 Logging out...")
                    return
                }


                3 -> if (currentUser.userRole == UserRole.ADMIN) {
                    ui.displayMessage("🔙 Logging out...")
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