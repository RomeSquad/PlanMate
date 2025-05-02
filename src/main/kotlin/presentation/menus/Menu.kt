package org.example.presentation.menus

class Menu(
    val actions: List<MenuAction>
) {
    fun getAction(index: Int): MenuAction {
        if (index !in 0..actions.size) throw IllegalArgumentException("Invalid index")
        return actions[index - 1]
    }

    fun getActions(): List<MenuAction> = actions
}