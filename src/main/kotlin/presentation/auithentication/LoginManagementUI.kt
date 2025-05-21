package org.example.presentation.auithentication

import org.example.logic.usecase.auth.LoginUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class LoginManagementUI(
    private val loginUseCase: LoginUseCase,
    private val mainMenuUI: MainMenuUI
) : BaseMenuAction() {

    override val title: String = "Login Menu"

    private val menuOptions = listOf(
        MenuOption(1, "Login", action = ::handleLogin, icon = "🔑"),
        MenuOption(2, "Exit", action = ::handleExit, icon = "🚪")
    )

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runMenuLoop(ui, inputReader, menuOptions) { it.number == 2 }
    }

    private suspend fun handleLogin(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val credentials = collectCredentials(ui, inputReader)
            val user = loginUseCase.login(credentials.username, credentials.password)
            setCurrentUser(user)
            ui.displayMessage("🎉 Login successful! Welcome, ${user.username} (${user.userRole}).")
            mainMenuUI.execute(ui, inputReader)
        }
    }

    private fun handleExit(ui: UiDisplayer, inputReader: InputReader) {
        if (confirmAction(ui, inputReader, "🔹 Are you sure you want to exit? (y/n): ")) {
            throw ExitApplicationException()
        }
    }
}