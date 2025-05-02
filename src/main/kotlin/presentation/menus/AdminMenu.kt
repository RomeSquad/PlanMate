package org.example.presentation.menus

import presentation.App

class AdminMenu(
    actions: List<MenuAction>,
    app: App
) : Menu(
    actions = actions,
    app = app
)