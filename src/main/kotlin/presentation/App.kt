package presentation

import org.example.presentation.menus.MainMenu
import org.example.presentation.menus.Menu
import org.koin.java.KoinJavaComponent.getKoin
import presentation.io.InputReader
import presentation.io.UiDisplayer

class App(
    private val uiDisplayer: UiDisplayer,
    private val inputReader: InputReader,

    ) {

    private var menu: Menu = getKoin().get()

    fun start() {
        do {
            processUserMenuSelection()
        } while (shouldContinue())
        uiDisplayer.displayMessage("Goodbye")
    }

    fun changeMenu(menu: Menu) {
        this.menu = menu
    }

    private fun processUserMenuSelection() {
        try {
            displayMenuAndExecuteAction()
        } catch (e: IllegalArgumentException) {
            uiDisplayer.displayError("Invalid input provided")
        } catch (e: Exception) {
            uiDisplayer.displayError(e.message)
        }
    }

    private fun displayMenuAndExecuteAction() {
        uiDisplayer.displayMenu(menu.getActions())
        val input = inputReader.readIntOrNull() ?: throw IllegalArgumentException("Invalid input")
        val selectedAction = menu.getAction(input)
        selectedAction.execute(uiDisplayer, inputReader)
    }

    private fun shouldContinue(): Boolean {
        uiDisplayer.displayPrompt("Do you want to perform another action? (y/n): ")
        return inputReader.readString().equals("y", ignoreCase = true)
    }

}