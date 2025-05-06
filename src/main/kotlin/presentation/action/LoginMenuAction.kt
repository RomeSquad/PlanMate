package org.example.presentation.action

import org.example.logic.entity.auth.UserRole
import org.example.logic.usecase.auth.LoginUseCase
import org.example.presentation.menus.MenuAction
import presentation.io.InputReader
import presentation.io.UiDisplayer

class LoginMenuAction(
    private val loginUseCase: LoginUseCase,
    private val ui: UiDisplayer,
    private val inputReader: InputReader,
    override val description: String = "Login",
    val navigateToMateScreen: () -> Unit = {},
    val navigateToAdminScreen: () -> Unit = {},
    val retryAgain: () -> Unit = {},
    val navigateBack: () -> Unit = {}
) : MenuAction {

    operator fun invoke() {
        ui.displayMessage("Enter username:")
        val username = inputReader.readString()

        ui.displayMessage("Enter  password:")
        val password = inputReader.readString()

        val result = loginUseCase.login(username, password)
        if (result.isSuccess) {
            ui.displayMessage("User Login Successfully")
            ui.displayMessage("Welcome ${result.getOrNull()?.username}")
            result.getOrNull()?.let {
                if(it.userRole==UserRole.MATE) navigateToMateScreen()
                else navigateToAdminScreen()
            }
        } else {
            ui.displayMessage("Failed To Login : ${result.exceptionOrNull()?.message}")
            ui.displayMessage("Do you want to retry? (yes/no)")
            if (inputReader.readString() in "yes") {
                retryAgain()
            } else {
                navigateBack()
            }
        }
    }
}





