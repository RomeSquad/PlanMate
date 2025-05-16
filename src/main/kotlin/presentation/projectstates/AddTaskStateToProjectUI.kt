package org.example.presentation.projectstates

import kotlinx.coroutines.runBlocking
import org.example.logic.entity.ProjectState
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.state.AddTaskStateToProjectUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.util.*

class AddTaskStateToProjectUI(
    private val addTaskStateToProjectUseCase: AddTaskStateToProjectUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║ Add Task State to Project║
        ╚══════════════════════════╝
        """.trimIndent()

    override val menu: Menu = Menu()

    override suspend fun execute(
        ui: UiDisplayer,
        inputReader: InputReader
    ) {
        ui.displayMessage(description)
        val currentUser = getCurrentUserUseCase.getCurrentUser()
            ?: throw IllegalArgumentException("No authenticated user found! Please log in first.")
        ui.displayMessage("🔹 Current User: ${currentUser.username}")
        val state = promptForStateDetails(
            projectId = UUID.randomUUID(), // Replace with actual project ID
            ui = ui,
            inputReader = inputReader
        ) ?: return
        tryAddState(
            state = state,
            userId = currentUser.userId,
            ui = ui
        )
    }

    private fun promptForStateDetails(
        projectId: UUID,
        ui: UiDisplayer,
        inputReader: InputReader
    ): ProjectState? {
        val name = promptForStateName(
            ui = ui,
            inputReader = inputReader
        )

        return ProjectState(
            stateName = name,
            projectId = projectId
        )
    }

    private fun promptForStateName(ui: UiDisplayer, inputReader: InputReader): String {
        while (true) {
            ui.displayMessage("Enter the name of the new state:")
            val name = inputReader.readString(
                "State Name: "
            )
            if (name.isNotBlank()) return name
            ui.displayMessage("State name cannot be blank. Please try again.")
        }
    }

    private fun tryAddState(state: ProjectState, userId: UUID, ui: UiDisplayer) {
        try {
            runBlocking {
                addTaskStateToProjectUseCase.execute(state, userId)
                ui.displayPrompt("State '${state.stateName}' added to project successfully.")
            }
        } catch (e: Exception) {
            ui.displayError("Failed to add state${e.message}")
        }
    }
}