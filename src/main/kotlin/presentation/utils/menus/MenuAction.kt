package org.example.presentation.utils.menus

import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.io.InputReader

interface MenuAction {
    val description: String
    val menu: Menu

    suspend fun execute(
        ui: UiDisplayer,
        inputReader: InputReader
    )

}
