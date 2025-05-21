package org.example.presentation.user.admin

import org.example.logic.usecase.auth.GetAllUsersUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class ViewAllUserUI(
    private val getAllUsersUseCase: GetAllUsersUseCase
) : BaseMenuAction() {

    override val title: String = "All Users Viewer"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val users = fetchEntities(ui, { getAllUsersUseCase.getAllUsers() }, "users")
            displayEntities(
                ui, users, "Users",
                { user, index -> "👤 $index. ${user.username} | Role: ${user.userRole} | ID: ${user.userId}" }
            )
        }
    }
}