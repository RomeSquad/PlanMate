package org.example.presentation.auithentication

import org.example.logic.entity.auth.UserRole
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
            val currentUser = getCurrentUser.getCurrentUser()
            if (currentUser == null) {
                ui.displayMessage("❌ User not logged in.")
                return
            }

            showHeader(ui)
            val options = getMenuOptions(currentUser.userRole)
            showMenu(ui, options)

            val choice = readUserChoice(inputReader)
            if (!handleChoice(choice, currentUser.userRole, ui, inputReader)) {
                return // Exit when logout is selected
            }

            pause(ui, inputReader)
        }
    }

    private fun showHeader(ui: UiDisplayer) {
        ui.displayMessage(description)
    }

    private fun getMenuOptions(role: UserRole): List<String> {
        return if (role == UserRole.ADMIN) {
            listOf("👑 1. Admin Management", "📋 2. Mate Management", "🚪 3. Logout")
        } else {
            listOf("📋 1. Mate Management", "🚪 2. Logout")
        }
    }

    private fun showMenu(ui: UiDisplayer, options: List<String>) {
        ui.displayMessage("🔹 Please choose an option:")
        ui.displayMessage(options.joinToString("\n"))
        ui.displayMessage("🔹 Choose an option (1-${options.size}):")
    }

    private fun readUserChoice(inputReader: InputReader): Int? {
        return inputReader.readString("Choice: ").trim().toIntOrNull()
    }

    private suspend fun handleChoice(
        choice: Int?,
        role: UserRole,
        ui: UiDisplayer,
        inputReader: InputReader
    ): Boolean {
        return when (choice) {
            1 -> {
                if (role == UserRole.ADMIN) {
                    adminManagementUI.execute(ui, inputReader)
                } else {
                    mateManagementUI.execute(ui, inputReader)
                }
                true
            }

            2 -> {
                if (role == UserRole.ADMIN) {
                    mateManagementUI.execute(ui, inputReader)
                    true
                } else {
                    ui.displayMessage("🔙 Logging out...")
                    false
                }
            }

            3 -> {
                if (role == UserRole.ADMIN) {
                    ui.displayMessage("🔙 Logging out...")
                    false
                } else {
                    ui.displayMessage("❌ Invalid option.")
                    true
                }
            }

            else -> {
                ui.displayMessage("❌ Invalid option.")
                true
            }
        }
    }

    private fun pause(ui: UiDisplayer, inputReader: InputReader) {
        ui.displayMessage("🔄 Press Enter to continue...")
        inputReader.readString("")
    }

}