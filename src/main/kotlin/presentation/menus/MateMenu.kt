package org.example.presentation.menus

import presentation.App

class MateMenu(
    actions: List<MenuAction>,
    app: App
) : Menu(
    actions = actions,
    app = app
)