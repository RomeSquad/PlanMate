package org.example.presentation.history

import org.example.logic.usecase.history.ShowTaskHistoryUseCase
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.task.GetTasksByProjectIdUseCase
import org.example.presentation.utils.formatter.dataFormatter.format
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction

class ShowTaskHistoryUI(
    private val showTaskHistoryUseCase: ShowTaskHistoryUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
) : BaseMenuAction() {

    override val title: String = "Get Task History"

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
            val tasks =
                fetchTasksForProject(ui, selectedProject, { id -> getTasksByProjectIdUseCase.getTasksByProjectId(id) })
            val selectedTask = selectEntity(
                ui, inputReader, tasks, "Tasks",
                prompt = "🔹 Select a task to view history (1-${tasks.size}): ",
                format = { task, index -> "✅ $index. ${task.title} | ID: ${task.taskId}" }
            ) ?: run {
                ui.displayMessage("❌ No tasks found for project '${selectedProject.name}'.")
                return@executeWithErrorHandling
            }
            ui.displayMessage("🔍 Fetching change history for task '${selectedTask.title}'...")
            val logs = showTaskHistoryUseCase.execute(selectedTask.taskId)
            formatHistoryLogs(ui, logs, "Task: '${selectedTask.title}'") { log ->
                log.format()
            }
        }
    }
}