package org.example.presentation.menus

import presentation.io.InputReader
import presentation.io.UiDisplayer

interface MenuAction {
    val description: String
    val menu : Menu

    suspend fun execute (
        ui: UiDisplayer,
        inputReader: InputReader
    )

}
