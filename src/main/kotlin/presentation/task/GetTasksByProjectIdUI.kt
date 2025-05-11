package org.example.presentation.task

import org.example.logic.usecase.task.GetTasksByProjectIdUseCase
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import org.example.presentation.utils.io.InputReader
import java.text.SimpleDateFormat
import java.util.*

class GetTasksByProjectIdUI(
    private val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase,
) : MenuAction {
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override val description: String = """
        ╔══════════════════════════╗
        ║   Tasks by Project ID    ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Enter Project ID:")
            val projectIdInput = inputReader.readString("Project ID: ").trim()
            val projectId =
                projectIdInput.toIntOrNull() ?: throw IllegalArgumentException("Project ID must be a number")

            val tasks = getTasksByProjectIdUseCase.getTasksByProjectId(projectId)
            if (tasks.isEmpty()) {
                ui.displayMessage("❌ No tasks found for project ID '$projectId'.")
            } else {
                ui.displayMessage("✅ Tasks for Project ID '$projectId':")
                tasks.forEachIndexed { index, task ->
                    ui.displayMessage(
                        """
                        Task ${index + 1}:
                        ID: ${task.id}
                        Title: ${task.title}
                        Description: ${task.description}
                        State: ${task.state.stateName}
                        Project ID: ${task.projectId}
                        Created By: ${task.createdBy}
                        Created At: ${dateFormatter.format(Date(task.createdAt))}
                        Updated At: ${dateFormatter.format(Date(task.updatedAt))}
                        --------------------
                        """.trimIndent()
                    )
                }
            }

            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to retrieve tasks"}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}