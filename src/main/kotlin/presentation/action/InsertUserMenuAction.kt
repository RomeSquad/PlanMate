package org.example.presentation.action

import org.example.logic.entity.auth.UserRole
import org.example.presentation.menus.Menu
import org.example.logic.usecase.auth.InsertUserUseCase
import org.example.presentation.menus.MenuAction
import presentation.io.InputReader
import presentation.io.UiDisplayer

class InsertUserMenuAction(
    private val insertUserUseCase: InsertUserUseCase,
    override val description: String = "Insert User",
    override var menu: Menu,
) : MenuAction {
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {

        ui.displayMessage("Enter username:")
        val username = inputReader.readString()

        ui.displayMessage("Enter  password:")
        val password = inputReader.readString()

        ui.displayMessage("Enter  role: admin or mate:")
        val userRole: UserRole = inputReader.readString().let {
            when (it) {
                "admin" -> UserRole.ADMIN
                "mate" -> UserRole.MATE
                else -> {
                    ui.displayMessage("Invalid role. Defaulting to USER.")
                    UserRole.MATE
                }
            }
        }

        try {
            val result = insertUserUseCase.insertUser(username, password, userRole)
            ui.displayMessage("User inserted successfully")
        } catch (e: Exception) {
            ui.displayError("Error: ${e.message}")
        }


    }
}