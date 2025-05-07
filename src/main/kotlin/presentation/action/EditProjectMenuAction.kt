package org.example.presentation.action

import org.example.logic.entity.Project
import org.example.logic.entity.State
import logic.usecase.project.EditProjectUseCase
import org.example.presentation.menus.Menu
import org.example.presentation.menus.MenuAction
import presentation.io.InputReader
import presentation.io.UiDisplayer


class EditProjectMenuAction(
    private val editProjectUseCase: EditProjectUseCase,
    override val menu: Menu
) : MenuAction {

    override val description = "Edit existing project"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        ui.displayPrompt("Enter project ID to edit:")
        val id = inputReader.readIntOrNull() ?: return ui.displayError("Invalid ID")

        ui.displayPrompt("Enter new project name:")
        val name = inputReader.readString()

        ui.displayPrompt("Enter new description:")
        val description = inputReader.readString()

        val updatedProject = Project(
            id = id,
            name = name,
            description = description,
            changeHistory = listOf(),
            state = State(projectId = id.toString(), stateName = "In progress")
        )

        editProjectUseCase.execute(updatedProject)
        ui.displayMessage("Project updated successfully.")
    }
}
