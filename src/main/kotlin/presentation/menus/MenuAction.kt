package org.example.presentation.menus

import org.example.presentation.io.InputReader
import org.example.presentation.io.UiDisplayer

interface MenuAction {
    val description: String
    val menu : Menu

    suspend fun execute (
        ui: UiDisplayer,
        inputReader: InputReader
    )

}
