package org.example.presentation.auithentication

import org.example.logic.entity.auth.UserRole
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.presentation.user.admin.AdminManagementUI
import org.example.presentation.user.mate.MateManagementUI
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction


class MainMenuUI(
    private val adminManagementUI: AdminManagementUI,
    private val mateManagementUI: MateManagementUI,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : BaseMenuAction() {

    override val title: String = "Main Menu"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val currentUser = requireCurrentUser(getCurrentUserUseCase)
            val menuOptions = getRoleBasedMenuOptions(currentUser.userRole).map {
                when (it.number) {
                    1 -> it.copy(menuAction = if (currentUser.userRole == UserRole.ADMIN) adminManagementUI else mateManagementUI)
                    2 -> if (currentUser.userRole == UserRole.ADMIN) it.copy(menuAction = mateManagementUI) else it
                    3 -> it // Logout action is already set in getRoleBasedMenuOptions
                    else -> it
                }
            }
            runMenuLoop(
                ui,
                inputReader,
                menuOptions
            ) { option -> option.number == if (currentUser.userRole == UserRole.ADMIN) 3 else 2 }
        }
    }

    private fun getRoleBasedMenuOptions(role: UserRole): List<MenuOption> {
        return if (role == UserRole.ADMIN) {
            listOf(
                MenuOption(1, "Admin Management", icon = "👑"),
                MenuOption(2, "Mate Management", icon = "📋"),
                MenuOption(3, "Logout", action = ::handleLogout, icon = "🚪")
            )
        } else {
            listOf(
                MenuOption(1, "Mate Management", icon = "📋"),
                MenuOption(2, "Logout", action = ::handleLogout, icon = "🚪")
            )
        }
    }

    private fun handleLogout(ui: UiDisplayer, inputReader: InputReader) {
        ui.displayMessage("🔙 Logging out...")
        setCurrentUser(null)
    }
}