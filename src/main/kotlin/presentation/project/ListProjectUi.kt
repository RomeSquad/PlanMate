package org.example.presentation.project

import org.example.logic.entity.Project
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.formatter.CliFormatter
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction


class ListProjectUi(
    private val listProjectsUseCase: GetAllProjectsUseCase
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║  Project Details Viewer  ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        runCatching {
            ui.displayMessage(description)
            val projects = fetchProjects(ui)
            displayProjects(ui, projects)
            // Prompt to continue only on success
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }.onFailure { exception ->
            handleError(ui, exception)
        }
    }

    private suspend fun fetchProjects(ui: UiDisplayer): List<Project> {
        ui.displayMessage("🔄 Fetching all projects...")
        return listProjectsUseCase.getAllProjects()
    }

    private fun displayProjects(ui: UiDisplayer, projects: List<Project>) {
        if (projects.isEmpty()) {
            ui.displayMessage("❌ No projects found.")
            return
        }
        ui.displayMessage("✅ Projects:")
        val formatter = CliFormatter()
        projects.forEach { project ->
            ui.displayMessage(
                formatter.rectangleLayout(
                    """
                    Project Name: ${project.name}
                    Project Description: ${project.description}
                    Project State: ${project.state.stateName}
                    """.trimIndent(),
                    width = 50,
                    height = 3
                )
            )
        }
    }

    private fun handleError(ui: UiDisplayer, exception: Throwable) {
        ui.displayMessage("❌ An unexpected error occurred: ${exception.message ?: "Failed to retrieve projects"}")
    }
}