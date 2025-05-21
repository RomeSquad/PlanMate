package org.example.presentation.projectstates

import org.example.logic.entity.ProjectState
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.state.AddTaskStateToProjectUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class AddTaskStateToProjectUI(
    private val addTaskStateToProjectUseCase: AddTaskStateToProjectUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : BaseMenuAction() {

    override val title: String = "Add Task State to Project"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val currentUser = getCurrentUser(getCurrentUserUseCase)
            if (currentUser == null) {
                ui.displayMessage("❌ User not logged in.")
                return@executeWithErrorHandling
            }
            ui.displayMessage("🔹 Current User: ${currentUser.username} | ID: ${currentUser.userId}")
            val projects = fetchEntities(ui, { getAllProjectsUseCase.getAllProjects() }, "projects")
            val selectedProject = selectEntity(
                ui, inputReader, projects, "Projects",
                format = { project, index -> "📌 $index. ${project.name} | ID: ${project.projectId}" }
            ) ?: run {
                ui.displayMessage("❌ No projects available to add task states to!")
                return@executeWithErrorHandling
            }
            val stateName = readValidatedInput(
                ui, inputReader, "🔹 Enter task state name:", "Task State Name", "Task state name must not be blank",
                ::nonBlankValidator
            )
            val state = ProjectState(stateName = stateName, projectId = selectedProject.projectId)
            if (!confirmAction(
                    ui, inputReader,
                    "⚠️ Add task state '$stateName' to project '${selectedProject.name}' (ID: ${selectedProject.projectId})? [y/n]: "
                )
            ) {
                ui.displayMessage("🛑 Task state addition canceled.")
                return@executeWithErrorHandling
            }
            ui.displayMessage("🔹 Adding task state to project...")
            addTaskStateToProjectUseCase.execute(state, currentUser.userId)
            ui.displayMessage("✅ Task state '$stateName' added successfully to project '${selectedProject.name}'! 🎉")
        }
    }
}