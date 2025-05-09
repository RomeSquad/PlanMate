package org.example.presentation.audit

import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.example.presentation.utils.menus.MenuAction
import presentation.io.InputReader


class ShowAuditByProjectIdUI(
    override val description: String,
    override val menu: Menu
) : MenuAction {
    override suspend fun execute(ui: UiDisplayer, inputReader: InputReader) {
        TODO("Not yet implemented")
    }
}