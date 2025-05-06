package org.example.presentation.action

import org.example.logic.entity.auth.UserRole
import org.example.logic.usecase.auth.LoginUseCase
import org.example.presentation.menus.Menu
import org.example.presentation.menus.MenuAction
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin
import presentation.io.InputReader
import presentation.io.UiDisplayer

class LoginMenuAction(
    private val loginUseCase: LoginUseCase,
    override val description: String = "Login",
    override var menu: Menu,
) : MenuAction {
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {

        ui.displayMessage("Enter username:")
        val username = inputReader.readString()

        ui.displayMessage("Enter  password:")
        val password = inputReader.readString()

        try {
            val result = loginUseCase.login(username, password)
            if (result.userRole == UserRole.ADMIN)
                menu.setActions(getKoin().get((named("adminMenuActions"))))
            else if (result.userRole == UserRole.MATE)
                menu.setActions(getKoin().get((named("mateMenuActions"))))
        } catch (e: Exception) {
            ui.displayError("Error: ${e.message}")
        }


    }
}