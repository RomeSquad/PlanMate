package org.example.presentation.task

import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.example.logic.usecase.task.GetTasksByProjectIdUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.BaseMenuAction


class SwimlanesView(
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase
) : BaseMenuAction() {

    override val title: String = "All Tasks Viewer"

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        executeWithErrorHandling(ui, inputReader) {
            val projects = fetchEntities(ui, { getAllProjectsUseCase.getAllProjects() }, "projects")
            val selectedProject = selectEntity(
                ui, inputReader, projects, "Projects",
                format = { project, index -> "📌 $index. ${project.name} | ID: ${project.projectId}" }
            ) ?: run {
                ui.displayMessage("❌ No projects available.")
                return@executeWithErrorHandling
            }
            val tasks =
                fetchTasksForProject(ui, selectedProject, { id -> getTasksByProjectIdUseCase.getTasksByProjectId(id) })
            displayEntities(
                ui, tasks, "Tasks for Project '${selectedProject.name}'",
                { task, index ->
                    "📝 $index. Task ID: ${task.taskId} | Title: ${task.title} | Created At: ${formatDate(task.createdAt)}"
                }
            )
        }
    }
}