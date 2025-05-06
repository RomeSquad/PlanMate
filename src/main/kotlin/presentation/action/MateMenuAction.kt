package org.example.presentation.action

import org.example.presentation.menus.MenuAction
import org.example.presentation.navigation.Route
import presentation.io.InputReader
import presentation.io.UiDisplayer

class MateMenuAction(
    private val ui: UiDisplayer,
    private val inputReader: InputReader,
    override val description: String = "MateMenu",
    val retryAgain: () -> Unit = {},
    val navigateBack: () -> Unit = {},
    val exit: () -> Unit = {}
) : MenuAction {

    operator fun invoke() {
        ui.displayMessage("Choose an action:")

        MateActions.entries.map { enum ->
            ui.displayMessage("${enum.ordinal +1}. ${enum.name}")
        }.ifEmpty { ui.displayMessage("No actions available") }

        val choice = inputReader.readIntOrNull()
        if (choice == null || choice !in 1..MateActions.entries.size +1) {
            ui.displayMessage("Invalid choice. Please try again.")
            retryAgain()
        }
        when (choice) {
            MateActions.BACK.ordinal + 1 -> navigateBack()
            MateActions.EXIT.ordinal + 1 -> exit()
        }

    }
}
enum class MateActions(val route : Route?) {
    BACK(null),
    EXIT(null);
}
enum class AdminActions(val route : Route?) {
    INSERT_PROJECT(Route.InsertProjectScreen),
    EDIT_PROJECT(Route.EditProjectScreen),
    INSERT_USER(Route.InsertUserScreen),
    BACK(null),
    EXIT(null);
}