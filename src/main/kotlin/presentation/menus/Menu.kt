package org.example.presentation.menus

class Menu {
    private var actions: List<MenuAction> = listOf()
    fun setActions(actions: List<MenuAction>) {
        this.actions = actions
    }
}