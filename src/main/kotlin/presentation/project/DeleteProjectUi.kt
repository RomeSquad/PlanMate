package org.example.presentation.project

import org.example.logic.usecase.project.DeleteProjectByIdUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class DeleteProjectUi(
    private val deleteProjectUseCase: DeleteProjectByIdUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
) : MenuAction {
    override val description: String = """
        ╔════════════════════════════╗
        ║    Delete a Project        ║
        ╚════════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Fetching all projects...")
            val projects = getAllProjectsUseCase.getAllProjects()
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available for deletion.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("📂 Available Projects:")
            projects.forEachIndexed { index, project ->
                ui.displayMessage("📌 ${index + 1}. ${project.name} | 🆔 ID: ${project.projectId}")
            }
            val projectIndex =
                inputReader.readIntOrNull("🔹 Select a project to delete (1-${projects.size}): ", 1..projects.size)
                    ?.minus(1)
            if (projectIndex == null || projectIndex < 0 || projectIndex >= projects.size) {
                ui.displayMessage("❌ Invalid selection. Please try again.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            val selectedProject = projects[projectIndex]
            ui.displayMessage("🔹 You selected: ${selectedProject.name} | 🆔 ID: ${selectedProject.projectId}")
            val confirmation =
                inputReader.readString("⚠️ Are you sure you want to delete this project? [y/n]: ").trim().lowercase()
            if (confirmation != "y" && confirmation != "yes") {
                ui.displayMessage("❌ Deletion canceled.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("🔄 Deleting project...")
            deleteProjectUseCase.deleteProjectById(selectedProject.projectId)
            ui.displayMessage("🔹 Deleting project '${selectedProject.name}'...")
            ui.displayMessage("✅ Project '${selectedProject.name}' deleted successfully!")
            getAllProjectsUseCase.getAllProjects()
            ui.displayMessage("🔄 Please wait...")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to delete project"}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}