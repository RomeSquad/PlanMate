package org.example.presentation.project

import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import presentation.io.InputReader


class ListProjectUi(
    private val listProjectsUseCase: GetAllProjectsUseCase,
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
            val result = listProjectsUseCase.getAllProjects()
            result.fold(
                onSuccess = { projects ->
                    if (projects.isEmpty()) {
                        ui.displayMessage("❌ No projects found.")
                    } else {
                        ui.displayMessage("✅ Projects:")
                        projects.forEach { project ->
                            ui.displayMessage(
                                """
                                ╔══════════════════════════╗
                                ║ Project ID: ${project.id} ║
                                ║ Project Name: ${project.name} ║
                                ║ Project Description: ${project.description} ║
                                ║ Project State: ${project.state.stateName} ║
                                ╚══════════════════════════╝
                                """.trimIndent()
                            )
                        }
                    }
                },
                onFailure = { error ->
                    ui.displayMessage("❌ Failed to retrieve projects: ${error.message}")
                }
            )
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to retrieve projects"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}