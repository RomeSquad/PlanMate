package presentation

import org.example.presentation.CLIMenu
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu


class App(
    private val uiDisplayer: UiDisplayer,
    private val inputReader: InputReader,
    private val menu: Menu,
    private val cliMenu: CLIMenu
) {
    suspend fun start() {
        initializeMenu()
        try {
            runMainLoop()
        } catch (e: ExitApplicationException) {
            uiDisplayer.displayMessage("✅ Application closed ${e.message}.")
        }
    }

    private fun initializeMenu() {
        menu.setActions(listOf(cliMenu))
    }

    private suspend fun runMainLoop() {
        while (true) {
            try {
                cliMenu.execute(uiDisplayer, inputReader)
                break // Exit loop if execution is successful
            } catch (e: ExitApplicationException) {
                throw e // Propagate to start()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception) {
        val message = when (e) {
            is IllegalArgumentException -> "❌ Invalid input: ${e.message ?: "Invalid data provided"}"
            else -> "❌ Error: ${e.message ?: "Unexpected error occurred"}"
        }
        uiDisplayer.displayMessage(message)
    }

    private class ExitApplicationException : Exception("User requested application exit")
}