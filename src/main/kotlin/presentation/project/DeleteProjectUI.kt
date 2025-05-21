package org.example.presentation.project

import org.example.logic.entity.Project
import org.example.logic.usecase.project.DeleteProjectByIdUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class DeleteProjectUI(
    private val deleteProjectUseCase: DeleteProjectByIdUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : BaseMenuAction() {

    override val title: String = "Delete a Project"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val projects = fetchEntities(ui, { getAllProjectsUseCase.getAllProjects() }, "projects")
            val selectedProject = selectEntity(
                ui, inputReader, projects, "Projects",
                format = { project, index -> "📌 $index. ${project.name} | ID: ${project.projectId}" }
            )
            if (selectedProject == null) {
                ui.displayMessage("❌ No projects available for deletion.")
                return@executeWithErrorHandling
            }
            if (!confirmAction(
                    ui,
                    inputReader,
                    "⚠️ Are you sure you want to delete '${selectedProject.name}'? [y/n]: "
                )
            ) {
                ui.displayMessage("❌ Deletion canceled.")
                return@executeWithErrorHandling
            }
            deleteProject(ui, selectedProject)
            ui.displayMessage("✅ Project '${selectedProject.name}' deleted successfully!")
        }
    }

    private suspend fun deleteProject(ui: UiDisplayer, project: Project) {
        ui.displayMessage("🔄 Deleting project...")
        deleteProjectUseCase.deleteProjectById(project.projectId)
    }
}