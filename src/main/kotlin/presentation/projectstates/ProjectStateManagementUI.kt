package org.example.presentation.projectstates

import org.example.logic.entity.auth.UserRole
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class ProjectStateManagementUI(
    addTaskStateToProjectUI: AddTaskStateToProjectUI,
    addStateToProjectUI: AddStateToProjectUI,
    editProjectStateUI: EditProjectStateUI,
    deleteStateToProjectUI: DeleteStateToProjectUI,
    getAllStatesPerProjectUI: GetAllStatesPerProjectUI,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : BaseMenuAction() {

    override val title: String = "Project State Management"

    private val menuOptions = listOf(
        MenuOption(1, "Add Task State", menuAction = addTaskStateToProjectUI),
        MenuOption(2, "Add State", menuAction = addStateToProjectUI),
        MenuOption(3, "Edit State", menuAction = editProjectStateUI),
        MenuOption(4, "Delete State", menuAction = deleteStateToProjectUI),
        MenuOption(5, "View All States", menuAction = getAllStatesPerProjectUI),
        MenuOption(6, "Back")
    )

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val currentUser = getCurrentUser(getCurrentUserUseCase)
            if (currentUser == null) {
                ui.displayMessage("❌ User not logged in.")
                return@executeWithErrorHandling
            }
            if (currentUser.userRole != UserRole.ADMIN) {
                ui.displayMessage("❌ Admin access required.")
                return@executeWithErrorHandling
            }
            runMenuLoop(ui, inputReader, menuOptions) { it.number == 6 }
        }
    }
}