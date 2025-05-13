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
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔄 Fetching all projects...")
            val projects = listProjectsUseCase.getAllProjects()
            displayProjects(ui, projects)
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to retrieve projects"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }

    private fun displayProjects(ui: UiDisplayer, projects: List<Project>) {
        if (projects.isEmpty()) {
            ui.displayMessage("❌ No projects found.")
        } else {
            ui.displayMessage("✅ Projects:")
            val formatter = CliFormatter()
            projects.forEach { project ->
                ui.displayMessage(
                    formatter.rectangleLayout(
                        " Project Name: ${project.name}\n" +
                                "Project Description: ${project.description}\n" +
                                "Project State: ${project.state.stateName}",
                        width = 50,
                        height = 3
                    )
                )
            }
        }
    }
}