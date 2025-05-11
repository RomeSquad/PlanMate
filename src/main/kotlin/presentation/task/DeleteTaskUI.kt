package org.example.presentation.task

import org.example.logic.usecase.task.DeleteTaskUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import java.util.UUID

class DeleteTaskUI(
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║   Task Deletion Menu     ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Enter Project ID:")
            val projectIdInput = inputReader.readString("Project ID: ").trim()
            val projectId = UUID.fromString(projectIdInput)
            ui.displayMessage("🔹 Enter Task ID:")
            val taskId = UUID.fromString(inputReader.readString("Task ID: ").trim())

            deleteTaskUseCase.deleteTask(projectId, taskId)

            ui.displayMessage("✅ Task '$taskId' deleted successfully from project '$projectId'!")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message}")
        }
    }
}