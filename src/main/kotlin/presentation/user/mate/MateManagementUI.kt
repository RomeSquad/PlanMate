package org.example.presentation.user.mate

import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.presentation.task.TaskManagementUI
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class MateManagementUI(
    taskManagementUI: TaskManagementUI,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : BaseMenuAction() {

    override val title: String = "Mate Control Center"

    private val menuOptions = listOf(
        MenuOption(1, "Manage Tasks", menuAction = taskManagementUI),
        MenuOption(2, "Back")
    )

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val currentUser = getCurrentUser(getCurrentUserUseCase)
            if (currentUser == null) {
                ui.displayMessage("❌ User not logged in.")
                return@executeWithErrorHandling
            }
            runMenuLoop(ui, inputReader, menuOptions) { it.number == 2 }
        }
    }
}