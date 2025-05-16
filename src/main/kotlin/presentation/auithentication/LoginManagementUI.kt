package org.example.presentation.auithentication

import org.example.logic.exception.InvalidCredentialsException
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.auth.LoginUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class LoginManagementUI(
    private val loginUseCase: LoginUseCase,
    private val mainMenuUI: MainMenuUI,
    private val getCurrentUser: GetCurrentUserUseCase
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║       Login Menu         ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        while (true) {
            ui.displayMessage(description)
            ui.displayMessage("🔑 1. Login\n🚪 2. Exit")
            ui.displayMessage("🔹 Choose an option (1-2):")
            val choice = inputReader.readString("Choice: ").trim().toIntOrNull()

            when (choice) {
                1 -> {
                    try {
                        ui.displayMessage("🔹 Enter Username:")
                        val username = inputReader.readString("Username: ").trim()
                        ui.displayMessage("🔹 Enter Password:")
                        val password = inputReader.readString("Password: ").trim()

                        if (username.isEmpty() || password.isEmpty()) {
                            ui.displayMessage("❌ Username and password cannot be empty.")
                            continue
                        }
                        val isLogin = loginUseCase.login(username, password)
                        val getCurrentUser = getCurrentUser.getCurrentUser()

                        if (getCurrentUser == null) {
                            ui.displayMessage("❌ No authenticated user found! Please log in first.")
                            continue
                        }
                        ui.displayMessage("🎉 Login successful! Welcome, ${isLogin.username} (${isLogin.userRole}).")
                        ui.displayMessage("🔄 Press Enter to continue...")
                        inputReader.readString("")
                        mainMenuUI.execute(ui, inputReader)
                        return
                    } catch (e: InvalidCredentialsException) {
                        ui.displayMessage("❌ Invalid username or password${e.message}.")
                    } catch (e: Exception) {
                        ui.displayMessage("❌ An unexpected error occurred: ${e.message}")
                    }
                }

                2 -> {
                    ui.displayMessage("🚪 Exiting...")
                    return
                }

                else -> {
                    ui.displayMessage("❌ Invalid option. Please select 1 or 2.")
                }
            }
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}