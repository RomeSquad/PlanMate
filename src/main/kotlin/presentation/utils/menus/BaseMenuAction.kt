package org.example.presentation.utils.menus

import org.example.logic.entity.Project
import org.example.logic.entity.Task
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.EmptyPasswordException
import org.example.logic.exception.EntityNotChangedException
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.example.presentation.auithentication.Session
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseMenuAction : MenuAction {
    override val menu: Menu = Menu()

    protected abstract val title: String

    override val description: String
        get() = """
            ╔════════════════════════════╗
            ║ $title    ║
            ╚══════════${"═".repeat(maxOf(0, 28 - title.length))}╝
        """.trimIndent()

    private val entityFetcher = EntityFetcher()

    protected suspend fun <T> fetchEntities(
        ui: UiDisplayer,
        fetchUseCase: suspend () -> List<T>,
        entityName: String
    ): List<T> {
        return entityFetcher.fetchEntities(ui, fetchUseCase, entityName)
    }

    protected suspend fun fetchTasksForProject(
        ui: UiDisplayer,
        project: Project,
        fetchUseCase: suspend (UUID) -> List<Task>
    ): List<Task> {
        return entityFetcher.fetchEntitiesForContext(
            ui, fetchUseCase, project.projectId, "tasks", "project '${project.name}'"
        )
    }

    protected suspend fun executeWithErrorHandling(
        ui: UiDisplayer,
        inputReader: InputReader,
        block: suspend () -> Unit
    ) {
        runCatching {
            block()
            promptToContinue(ui, inputReader)
        }.onFailure { exception ->
            handleError(ui, exception)
        }
    }

    protected fun <T> readValidatedInput(
        ui: UiDisplayer,
        inputReader: InputReader,
        prompt: String,
        label: String,
        errorMessage: String,
        validator: (String) -> T?,
        hint: String? = null
    ): T {
        ui.displayMessage(buildPrompt(prompt, hint))
        while (true) {
            val input = inputReader.readString("$label: ").trim()
            val result = validator(input)
            if (result != null) return result
            ui.displayMessage("❌ $errorMessage")
        }
    }

    protected fun nonBlankValidator(input: String): String? = input.takeIf { it.isNotBlank() }

    protected fun collectCredentials(ui: UiDisplayer, inputReader: InputReader): Credentials {
        val username = readValidatedInput(
            ui, inputReader, "🔹 Enter Username:", "Username", "Username cannot be empty",
            ::nonBlankValidator
        )
        val password = readValidatedInput(
            ui, inputReader, "🔹 Enter Password:", "Password", "Password cannot be empty",
            ::nonBlankValidator
        )
        return Credentials(username, password)
    }

    protected fun <T> displayEntities(
        ui: UiDisplayer,
        entities: List<T>,
        entityName: String,
        format: (T, Int) -> String
    ) {
        if (entities.isEmpty()) {
            ui.displayMessage("❌ No $entityName found.")
            return
        }
        ui.displayMessage("📋 Available $entityName:")
        entities.forEachIndexed { index, entity ->
            ui.displayMessage(format(entity, index + 1))
        }
    }

    protected fun <T> selectEntity(
        ui: UiDisplayer,
        inputReader: InputReader,
        entities: List<T>,
        entityName: String,
        prompt: String = "🔹 Select a $entityName (1-${entities.size}): ",
        format: (T, Int) -> String
    ): T? {
        displayEntities(ui, entities, entityName, format)
        if (entities.isEmpty()) return null
        val index = readValidatedInput(
            ui,
            inputReader,
            prompt,
            "Choice",
            "Invalid selection. Please select a number between 1 and ${entities.size}",
            { it.toIntOrNull()?.takeIf { it in 1..entities.size } }
        )
        return entities[index - 1]
    }

    protected open fun selectUserRole(
        ui: UiDisplayer,
        inputReader: InputReader,
        currentRole: UserRole? = null,
        prompt: String = if (currentRole != null) "🔹 Select New User Role (current: $currentRole):" else "🔹 Select User Role:"
    ): UserRole {
        val roleOptions = UserRole.entries.joinToString(", ") { it.name }
        return readValidatedInput(
            ui, inputReader, prompt, "Role", "Invalid role. Choose from $roleOptions or a number (1: ADMIN, 2: MATE).",
            {
                when {
                    it.isBlank() && currentRole != null -> currentRole
                    UserRole.entries.any { role -> role.name.equals(it, ignoreCase = true) } ->
                        UserRole.entries.find { role -> role.name.equals(it, ignoreCase = true) }

                    it == "1" -> UserRole.ADMIN
                    it == "2" -> UserRole.MATE
                    else -> null
                }
            },
            hint = "Options: $roleOptions, 1: ADMIN, 2: MATE${if (currentRole != null) ", empty to keep $currentRole" else ""}"
        )
    }

    protected fun confirmAction(
        ui: UiDisplayer,
        inputReader: InputReader,
        prompt: String
    ): Boolean {
        ui.displayMessage(prompt)
        val confirmation = readValidatedInput(
            ui, inputReader, "", "Confirm", "Please enter 'y' or 'n'",
            { it.trim().lowercase().takeIf { it == "y" || it == "yes" || it == "n" || it == "no" } }
        )
        return confirmation == "y" || confirmation == "yes"
    }

    protected fun promptToContinue(ui: UiDisplayer, inputReader: InputReader) {
        ui.displayMessage("🔄 Press Enter to continue...")
        inputReader.readString("")
    }

    protected fun formatDate(timestamp: Long): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormatter.format(Date(timestamp))
    }

    protected fun <T> formatHistoryLogs(
        ui: UiDisplayer,
        logs: List<T>,
        entityName: String,
        formatLog: (T) -> String
    ) {
        if (logs.isEmpty()) {
            ui.displayMessage("❌ No change history found for $entityName.")
            return
        }
        ui.displayMessage("📜 Change History for $entityName:")
        logs.map(formatLog).forEach { log ->
            ui.displayMessage(log)
            ui.displayMessage("-".repeat(100))
        }
    }

    protected fun handleError(ui: UiDisplayer, exception: Throwable) {
        val message = when (exception) {
            is IllegalArgumentException -> "❌ Invalid input: ${exception.message ?: "Invalid data provided"}"
            is IllegalStateException -> "❌ Error: ${exception.message ?: "Operation failed"}"
            is EntityNotChangedException -> "❌ No changes made: ${exception.message ?: "No updates provided"}"
            is EmptyPasswordException -> "❌ Password error: ${exception.message ?: "New password cannot be empty"}"
            is ExitApplicationException -> "🚪 Exiting application..."
            else -> {
                println("Unexpected error: ${exception.stackTraceToString()}")
                "❌ Unexpected error: ${exception.message ?: "An error occurred"}"
            }
        }
        ui.displayMessage(message)
        if (exception is ExitApplicationException) throw exception
    }

    protected suspend fun getCurrentUser(getCurrentUserUseCase: GetCurrentUserUseCase): User? {
        return getCurrentUserUseCase.getCurrentUser()
    }

    protected suspend fun requireCurrentUser(getCurrentUserUseCase: GetCurrentUserUseCase): User {
        return getCurrentUser(getCurrentUserUseCase)
            ?: throw IllegalStateException("No authenticated user found. Please log in.")
    }

    protected fun setCurrentUser(user: User?) {
        Session.currentUser = user
    }

    protected fun displayMenuOptions(ui: UiDisplayer, options: List<MenuOption>) {
        ui.displayMessage(description)
        options.forEach { option ->
            ui.displayMessage("${option.icon} ${option.number}. ${option.description}")
        }
        ui.displayMessage("🔹 Choose an option (1-${options.size}):")
    }

    protected suspend fun runMenuLoop(
        ui: UiDisplayer,
        inputReader: InputReader,
        options: List<MenuOption>,
        isExitOption: (MenuOption) -> Boolean
    ) {
        while (true) {
            displayMenuOptions(ui, options)
            val option = selectMenuOption(ui, inputReader, options) ?: continue
            if (isExitOption(option)) {
                ui.displayMessage("🔙 Exiting...")
                return
            }
            when {
                option.action != null -> option.action.invoke(ui, inputReader)
                option.menuAction != null -> option.menuAction.execute(ui, inputReader)
                else -> ui.displayMessage("❌ No action defined for this option.")
            }
        }
    }

    protected fun selectMenuOption(
        ui: UiDisplayer,
        inputReader: InputReader,
        options: List<MenuOption>
    ): MenuOption? {
        return readValidatedInput(
            ui, inputReader, "", "Choice", "Invalid selection. Please select a number between 1 and ${options.size}",
            { input ->
                input.toIntOrNull()?.let { choice ->
                    options.find { it.number == choice }
                }
            }
        )
    }

    protected data class MenuOption(
        val number: Int,
        val description: String,
        val action: (suspend (UiDisplayer, InputReader) -> Unit)? = null,
        val menuAction: MenuAction? = null,
        val icon: String = "🔹"
    )

    protected data class Credentials(
        val username: String,
        val password: String
    )

    protected class ExitApplicationException : Exception("User requested application exit")

    private fun buildPrompt(prompt: String, hint: String?): String =
        if (hint != null) "$prompt ($hint)" else prompt

}