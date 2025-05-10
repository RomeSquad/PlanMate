package presentation

import org.example.presentation.CLIMenu
import org.example.presentation.io.InputReader
import org.example.presentation.io.UiDisplayer
import org.example.presentation.menus.Menu

class App(
    private val uiDisplayer: UiDisplayer,
    private val inputReader: InputReader,
    private val menu: Menu,
    private val cliMenu: CLIMenu
    ) {
    private suspend fun processUserMenuSelection() {
        menu.setActions(listOf(cliMenu))
        try {
            cliMenu.execute(uiDisplayer, inputReader)
        } catch (e: IllegalArgumentException) {
            uiDisplayer.displayError("Invalid input provided")
            uiDisplayer.displayMessage("❌ Invalid input provided${e.message}")
            uiDisplayer.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: Exception) {
            uiDisplayer.displayMessage("❌ Error: ${e.message ?: "Unexpected error"}")
            uiDisplayer.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}