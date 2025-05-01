package presentation

import presentation.io.InputReader
import presentation.io.UiDisplayer

interface MenuAction {
    val description: String

    fun execute (
        ui: UiDisplayer,
        inputReader: InputReader
    )
}