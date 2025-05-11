package org.example.presentation.task

import org.example.logic.usecase.task.GetAllTasksUseCase
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import org.example.presentation.utils.io.InputReader
import java.text.SimpleDateFormat
import java.util.*

class GetAllTasksUI(
    private val getAllTasksUseCase: GetAllTasksUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║     All Tasks Viewer     ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            val tasks = getAllTasksUseCase.getAllTasks()
            if (tasks.isEmpty()) {
                ui.displayMessage("❌ No tasks found.")
            } else {
                ui.displayMessage("✅ All Tasks:")
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
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to retrieve tasks"}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}