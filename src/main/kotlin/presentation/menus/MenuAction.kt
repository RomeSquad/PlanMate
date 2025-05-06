package org.example.presentation.menus

import presentation.io.InputReader
import presentation.io.UiDisplayer

interface MenuAction {
    val description: String

    suspend fun execute (
        ui: UiDisplayer,
        inputReader: InputReader
    )

}
