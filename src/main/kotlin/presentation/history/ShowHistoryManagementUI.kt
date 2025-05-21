package org.example.presentation.history

import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class ShowHistoryManagementUI(
    private val showProjectHistoryUI: ShowProjectHistoryUI,
    private val showTaskHistoryUI: ShowTaskHistoryUI,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : BaseMenuAction() {

    override val title: String = "History Management"

    private val menuOptions = listOf(
        MenuOption(1, "Show Project History", menuAction = showProjectHistoryUI),
        MenuOption(2, "Show Task History", menuAction = showTaskHistoryUI),
        MenuOption(3, "Back")
    )

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val currentUser = getCurrentUser(getCurrentUserUseCase)
            if (currentUser == null) {
                ui.displayMessage("❌ User not logged in.")
                return@executeWithErrorHandling
            }
            runMenuLoop(ui, inputReader, menuOptions) { it.number == 3 }
        }
    }
}