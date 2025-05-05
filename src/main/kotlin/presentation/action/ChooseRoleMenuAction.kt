package org.example.presentation.action

import org.example.presentation.menus.Menu
import org.example.presentation.menus.MenuAction
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin
import presentation.io.InputReader
import presentation.io.UiDisplayer

class ChooseRoleMenuAction(
    override val description: String = "Choose your role",
    override val menu: Menu
) : MenuAction {

    override fun execute(ui: UiDisplayer, inputReader: InputReader) {
        ui.displayMessage("Choose your role:")
        ui.displayMessage("1. Admin")
        ui.displayMessage("2. Mate")
        ui.displayMessage("3. Exit")

        val choice = inputReader.readIntOrNull()
        when (choice) {
            1 -> {
                ui.displayMessage("You chose Admin")
                menu.setActions(getKoin().get((named("adminMenuActions"))))
            }
            2 -> {
                ui.displayMessage("You chose Mate")
                menu.setActions(getKoin().get((named("mateMenuActions"))))
            }
            3 -> ui.displayMessage("Exiting...")
            else -> ui.displayError("Invalid choice, please try again.")
        }
    }
}