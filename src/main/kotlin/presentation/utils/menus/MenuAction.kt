package org.example.presentation.utils.menus

import presentation.io.InputReader
import org.example.presentation.utils.io.UiDisplayer

interface MenuAction {
    val description: String
    val menu : Menu

    suspend fun execute (
        ui: UiDisplayer,
        inputReader: InputReader
    )

}
