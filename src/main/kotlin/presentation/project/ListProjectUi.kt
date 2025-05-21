package org.example.presentation.project

import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction


class ListProjectUI(
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : BaseMenuAction() {

    override val title: String = "List Projects"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val projects = fetchEntities(ui, { getAllProjectsUseCase.getAllProjects() }, "projects")
            displayEntities(
                ui, projects, "Projects",
                { project, index -> "📋 $index. ${project.name} (ID: ${project.projectId})" }
            )
        }
    }
}