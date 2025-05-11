package presentation

import org.example.presentation.CLIMenu
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.io.InputReader


class App(
    private val uiDisplayer: UiDisplayer,
    private val inputReader: InputReader,
    private val menu: Menu,
    private val cliMenu: CLIMenu
) {
    suspend fun start() {
        menu.setActions(listOf(cliMenu))
        try {
            cliMenu.execute(uiDisplayer, inputReader)
        } catch (e: IllegalArgumentException) {
            uiDisplayer.displayMessage("❌ Invalid input provided ${e.message}")
            uiDisplayer.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        } catch (e: Exception) {
            uiDisplayer.displayMessage("❌ Error: ${e.message ?: "Unexpected error"}")
            uiDisplayer.displayMessage("🔄 Press Enter to continue...")
            inputReader.readString("")
        }
    }
}