package org.example.presentation.task

import org.example.logic.usecase.task.EditTaskUseCase
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction

class EditTaskUI(
    private val editTaskUseCase: EditTaskUseCase,
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║    Task Edit Menu        ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Enter Task ID:")
            val taskId = inputReader.readString("Task ID: ").trim()

            ui.displayMessage("🔹 Enter New Task Title:")
            val title = inputReader.readString("Title: ").trim()

            ui.displayMessage("🔹 Enter New Task Description:")
            val description = inputReader.readString("Description: ").trim()

            editTaskUseCase.editTask(
                taskId = taskId,
                title = title,
                description = description,
                updatedAt = System.currentTimeMillis()
            )

            ui.displayMessage("✅ Task '$taskId' updated successfully!")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message}")
        }
    }
}