package org.example.presentation.history

import org.example.logic.usecase.history.ShowProjectHistoryUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.presentation.utils.formatter.dataFormatter.format
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class ShowProjectHistoryUI(
    private val showProjectHistoryUseCase: ShowProjectHistoryUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : BaseMenuAction() {

    override val title: String = "Get Project History"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val projects = fetchEntities(ui, { getAllProjectsUseCase.getAllProjects() }, "projects")
            val selectedProject = selectEntity(
                ui, inputReader, projects, "Projects",
                prompt = "🔹 Select a project (1-${projects.size}): ",
                format = { project, index -> "📂 $index. ${project.name} | ID: ${project.projectId}" }
            ) ?: run {
                ui.displayMessage("❌ No projects available.")
                return@executeWithErrorHandling
            }
            ui.displayMessage("🔍 Fetching change history for project '${selectedProject.name}'...")
            val logs = showProjectHistoryUseCase.execute(selectedProject.projectId)
            formatHistoryLogs(ui, logs, "Project: '${selectedProject.name}'") { log ->
                log.format()
            }
        }
    }
}