package org.example.presentation.projectstates

import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.state.DeleteProjectStatesUseCase
import org.example.logic.usecase.state.GetAllProjectStatesUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class DeleteStateToProjectUI(
    private val deleteProjectStatesUseCase: DeleteProjectStatesUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getAllProjectStatesUseCase: GetAllProjectStatesUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║ Delete State from Project║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Available Projects:")
            val projects = getAllProjectsUseCase.getAllProjects()
            if (projects.isEmpty()) {
                ui.displayMessage("❌ No projects available to delete states from!")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            projects.forEachIndexed { index, project ->
                ui.displayMessage("📌 ${index + 1}. ${project.name} (ID: ${project.projectId})")
            }
            ui.displayMessage("🔹 Select a project to delete states from (1-${projects.size}):")
            val selectedIndex = inputReader.readString("Choice: ").trim().toIntOrNull()
            if (selectedIndex == null || selectedIndex < 1 || selectedIndex > projects.size) {
                ui.displayMessage("❌ Invalid selection. Please select a valid project number.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            val selectedProject = projects[selectedIndex - 1]
            ui.displayMessage("🔹 You selected: ${selectedProject.name} (ID: ${selectedProject.projectId})")
            ui.displayMessage("🔹 Fetching all states for project '${selectedProject.name}'...")
            val states = getAllProjectStatesUseCase.execute(selectedProject.state)
            if (states.isEmpty()) {
                ui.displayMessage("❌ No states available to delete for project '${selectedProject.name}'!")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("🔹 Available States:")
            states.forEachIndexed { index, state ->
                ui.displayMessage("📌 ${index + 1}. ${state.stateName} (ID: ${state.projectId})")
            }
            ui.displayMessage("🔹 Select a state to delete (1-${states.size}):")
            val selectedStateIndex = inputReader.readString("Choice: ").trim().toIntOrNull()
            if (selectedStateIndex == null || selectedStateIndex < 1 || selectedStateIndex > states.size) {
                ui.displayMessage("❌ Invalid selection. Please select a valid state number.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            val selectedState = states[selectedStateIndex - 1]
            ui.displayMessage("🔹 You selected: ${selectedState.stateName} (ID: ${selectedState.projectId})")
            ui.displayMessage("🔹 Are you sure you want to delete this state? This action cannot be undone.")
            ui.displayMessage("⚠️ Type 'YES' to confirm deletion:")
            val confirmation = inputReader.readString("Confirm: ").trim()
            if (confirmation != "YES") {
                ui.displayMessage("🛑 State deletion canceled.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }
            ui.displayMessage("🔹 Deleting state '${selectedState.stateName}'...")
            val deleted = deleteProjectStatesUseCase.execute(selectedState.projectId)
            if (deleted) {
                ui.displayMessage("✅ State '${selectedState.stateName}' deleted successfully!")
            } else {
                ui.displayMessage("❌ Failed to delete state '${selectedState.stateName}'.")
            }
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")

        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to delete state"}")
        } finally {
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}