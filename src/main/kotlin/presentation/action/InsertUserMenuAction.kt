package org.example.presentation.action

import org.example.logic.entity.auth.UserRole
import org.example.logic.usecase.auth.InsertUserUseCase
import org.example.presentation.menus.MenuAction
import presentation.io.InputReader
import presentation.io.UiDisplayer

class InsertUserMenuAction(
    private val insertUserUseCase: InsertUserUseCase,
    override val description: String = "Insert User",
    private val ui: UiDisplayer ,
    private val inputReader: InputReader,
    private val navigateBack: () -> Unit = {}
) : MenuAction {
    operator fun invoke() {

        ui.displayMessage("Enter username:")
        val username = inputReader.readString()

        ui.displayMessage("Enter  password:")
        val password = inputReader.readString()

        ui.displayMessage("Enter  role:")
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

        val result = insertUserUseCase.insertUser(username, password, userRole)
        if (result.isSuccess) {
            ui.displayMessage("User inserted successfully")
            navigateBack()
        } else {
            ui.displayMessage("Error inserting user: ${result.exceptionOrNull()?.message}")
        }
    }
}