package org.example.presentation.utils.menus

class Menu {
    private var actions: List<MenuAction> = listOf()
    fun setActions(actions: List<MenuAction>) {
        this.actions = actions
    }
}