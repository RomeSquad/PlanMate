package org.example.presentation.task

import org.example.logic.usecase.task.GetTaskByIdUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.util.UUID

class GetTaskByIdUI(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║   Task Retrieval Menu    ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Enter Task ID:")
            val taskId = UUID.fromString(inputReader.readString("Task ID: ").trim())

            val task = getTaskByIdUseCase.getTaskById(taskId)

            ui.displayMessage(
                """
                ✅ Task Details:
                ID: ${task.taskId}
                Title: ${task.title}
                Description: ${task.description}
                Project ID: ${task.projectId}
                State: ${task.state}
                Created By: ${task.createdBy}
                Created At: ${task.createdAt}
                Updated At: ${task.updatedAt}
            """.trimIndent()
            )
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message}")
        }
    }
}