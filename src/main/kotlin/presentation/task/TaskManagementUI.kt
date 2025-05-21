package org.example.presentation.task

import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.presentation.history.ShowHistoryManagementUI
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class TaskManagementUI(
    createTaskUI: CreateTaskUI,
    deleteTaskUI: DeleteTaskUI,
    editTaskUI: EditTaskUI,
    showHistoryManagementUI: ShowHistoryManagementUI,
    swimlanesView: SwimlanesView,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : BaseMenuAction() {

    override val title: String = "Task Management"

    private val menuOptions = listOf(
        MenuOption(1, "Create Task", menuAction = createTaskUI),
        MenuOption(2, "Delete Task", menuAction = deleteTaskUI),
        MenuOption(3, "Edit Task", menuAction = editTaskUI),
        MenuOption(4, "View History", menuAction = showHistoryManagementUI),
        MenuOption(5, "View Swimlanes", menuAction = swimlanesView),
        MenuOption(6, "Back")
    )

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            if (getCurrentUser(getCurrentUserUseCase) == null) {
                ui.displayMessage("❌ User not logged in.")
                return@executeWithErrorHandling
            }
            runMenuLoop(ui, inputReader, menuOptions) { it.number == 6 }
        }
    }
}