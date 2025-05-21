package org.example.presentation.user.admin

import org.example.logic.entity.auth.UserRole
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.presentation.history.ShowHistoryManagementUI
import org.example.presentation.project.ProjectManagementUI
import org.example.presentation.task.TaskManagementUI
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class AdminManagementUI(
    projectManagementUI: ProjectManagementUI,
    taskManagementUI: TaskManagementUI,
    createUserUi: CreateUserUi,
    deleteUserUi: DeleteUserUi,
    editUserUI: EditUserUI,
    viewAllUserUI: ViewAllUserUI,
    changeHistoryManagementUI: ShowHistoryManagementUI,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : BaseMenuAction() {

    override val title: String = "Admin Control Center"

    private val menuOptions = listOf(
        MenuOption(1, "Manage Projects", menuAction = projectManagementUI),
        MenuOption(
            2,
            "Manage Users",
            menuAction = UserManagementSubMenu(createUserUi, deleteUserUi, editUserUI, viewAllUserUI)
        ),
        MenuOption(3, "Manage Tasks", menuAction = taskManagementUI),
        MenuOption(4, "View Audit Logs", menuAction = changeHistoryManagementUI),
        MenuOption(5, "Back")
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
            runMenuLoop(ui, inputReader, menuOptions) { it.number == 5 }
        }
    }

    private class UserManagementSubMenu(
        createUserUi: CreateUserUi,
        deleteUserUi: DeleteUserUi,
        editUserUI: EditUserUI,
        viewAllUserUI: ViewAllUserUI
    ) : BaseMenuAction() {

        override val title: String = "User Management"

        private val userMenuOptions = listOf(
            MenuOption(1, "Create User", menuAction = createUserUi),
            MenuOption(2, "Delete User", menuAction = deleteUserUi),
            MenuOption(3, "Edit User", menuAction = editUserUI),
            MenuOption(4, "View All Users", menuAction = viewAllUserUI),
            MenuOption(5, "Back")
        )

        override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
            executeWithErrorHandling(ui, inputReader) {
                runMenuLoop(ui, inputReader, userMenuOptions) { it.number == 5 }
            }
        }
    }
}