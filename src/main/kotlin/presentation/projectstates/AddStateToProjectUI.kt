package org.example.presentation.projectstates

import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.state.AddProjectStatesUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction


class AddStateToProjectUI(
    private val addProjectStatesUseCase: AddProjectStatesUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║  Add State to Project    ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Fetching current user...")
            val currentUser = getCurrentUser.getCurrentUser()
            if (currentUser == null) {
                ui.displayMessage("❌ No current user found. Please log in.")
                return
            }
            ui.displayMessage("🔹 Current User: ${currentUser.username} (ID: ${currentUser.userId})")
            ui.displayMessage("🔹 Fetching all projects...")
            val projects = getAllProjectsUseCase.getAllProjects()
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available to add states to!")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("👥 Available Projects:")
            projects.forEachIndexed { index, project ->
                ui.displayMessage("📌 ${index + 1}. ${project.name} (ID: ${project.projectId})")
            }
            ui.displayMessage("🔹 Select a project to add a state to (1-${projects.size}):")
            val selectedIndex = inputReader.readString("Choice: ").trim().toIntOrNull()
            if (selectedIndex == null || selectedIndex < 1 || selectedIndex > projects.size) {
                ui.displayMessage("❌ Invalid selection. Please select a valid project number.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            val selectedProject = projects[selectedIndex - 1]
            ui.displayMessage("🔹 You selected: ${selectedProject.name} (ID: ${selectedProject.projectId})")
            ui.displayMessage("🔹 Are you sure you want to add a state to this project?")
            ui.displayMessage("⚠️ Type 'YES' to confirm adding a state:")
            val confirmation = inputReader.readString("Confirm: ").trim()
            if (confirmation != "YES") {
                ui.displayMessage("🛑 State addition canceled.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("🔹 Adding state to project '${selectedProject.name}'...")
            addProjectStatesUseCase.execute(
                projectId = selectedProject.projectId,
                stateName = inputReader.readString("Enter state name: ").trim(),
            )
            ui.displayMessage("✅ State added successfully to project '${selectedProject.name}'!")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ Failed to add state to project: ${e.message ?: "Unexpected error"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}