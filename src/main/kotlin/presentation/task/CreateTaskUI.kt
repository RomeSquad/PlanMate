package org.example.presentation.task

import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import org.example.logic.usecase.task.CreateTaskUseCase
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import presentation.io.InputReader


class CreateTaskUI(
    private val createTaskUseCase: CreateTaskUseCase,
    private val currentUserId: String = "",
) : MenuAction {
    override val description: String = """
        ╔══════════════════════════╗
        ║    Create a New Task     ║
        ╚══════════════════════════╝
        """.trimIndent()
    override val menu: Menu = Menu()
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        try {
            ui.displayMessage(description)
            ui.displayMessage("🔹 Enter Task Title:")
            val title = inputReader.readString("Title: ").trim()
            if (title.isBlank()) {
                throw IllegalArgumentException("Title must not be blank")
            }

            ui.displayMessage("🔹 Enter Task Description:")
            val description = inputReader.readString("Description: ").trim()
            if (description.isBlank()) {
                throw IllegalArgumentException("Description must not be blank")
            }

            ui.displayMessage("🔹 Enter Project ID:")
            val projectIdInput = inputReader.readString("Project ID: ").trim()
            val projectId =
                projectIdInput.toIntOrNull() ?: throw IllegalArgumentException("Project ID must be a number")

            ui.displayMessage("🔹 Enter Task State Name (e.g., TODO, IN_PROGRESS, DONE, leave empty for TODO):")
            val stateName = inputReader.readString("State Name: ").trim().takeIf { it.isNotBlank() } ?: "TODO"

            val createdBy = currentUserId.ifEmpty {
                ui.displayMessage("🔹 Enter Created By (user ID or name):")
                inputReader.readString("Created By: ").trim().takeIf { it.isNotBlank() }
                    ?: throw IllegalArgumentException("Created By must not be blank")
            }

            val currentTime = System.currentTimeMillis()
            val task = Task(
                id = "",
                title = title,
                description = description,
                state = ProjectState(projectId = projectId, stateName = stateName),
                projectId = projectId,
                createdBy = createdBy,
                createdAt = currentTime,
                updatedAt = currentTime
            )

            ui.displayMessage("⚠️ Create task '$title' for project ID '$projectId'? [y/n]")
            val confirmation = inputReader.readString("Confirm: ").trim().lowercase()
            if (confirmation != "y") {
                ui.displayMessage("🛑 Task creation canceled.")
                ui.displayMessage("🔄 Press Enter to continue...")
                inputReader.readString("")
                return
            }

            createTaskUseCase.createTask(task)
            ui.displayMessage("✅ Task '$title' created successfully!")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: IllegalArgumentException) {
            ui.displayMessage("❌ Error: ${e.message}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: Exception) {
            ui.displayMessage("❌ An unexpected error occurred: ${e.message ?: "Failed to create task"}")
            ui.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}
