package org.example.presentation.action

import org.example.logic.usecase.auth.LoginUseCase
import org.example.presentation.menus.Menu
import org.example.presentation.menus.MenuAction
import presentation.io.InputReader
import presentation.io.UiDisplayer

class LoginMenuAction(
    private val loginUseCase: LoginUseCase,
    override val description: String = "Login",
    override var menu: Menu,
) : MenuAction {
    override fun execute(ui: UiDisplayer, inputReader: InputReader) {

        ui.displayMessage("Enter username:")
        val username = inputReader.readString()

        ui.displayMessage("Enter  password:")
        val password = inputReader.readString()


        val result = loginUseCase.login(username, password)
        if (result.isSuccess) {
            ui.displayMessage("User Login Successfully")
        } else {
            ui.displayMessage("Failed To Login : ${result.exceptionOrNull()?.message}")
        }
    }
}