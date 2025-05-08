package org.example.presentation.action

import org.example.logic.entity.CreateProjectRequest
import org.example.logic.usecase.project.InsertProjectUseCase
import org.example.presentation.menus.Menu
import org.example.presentation.menus.MenuAction
import presentation.io.InputReader
import presentation.io.UiDisplayer

class InsertProjectMenuAction(
    private val projectUseCase: InsertProjectUseCase,
    override val description: String = "Create new Project",
    override var menu : Menu
) : MenuAction {

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        ui.displayMessage("Enter project name:")
        val projectName = inputReader.readString()

        runCatching {
            val result = projectUseCase.insertProject(CreateProjectRequest(projectName, 9, "", ""))
            ui.displayMessage("Project created successfully: ${result.id}")
        }.onFailure {
            ui.displayError("Failed to create project: ${it.message}")
        }
    }
}