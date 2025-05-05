package org.example.presentation.menus

import org.example.presentation.action.ChooseRoleUseCase
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin
import presentation.App

class Menu {

    private var actions: List<MenuAction> = listOf<MenuAction>(
        ChooseRoleUseCase(menu= this)
    )

    fun getAction(index: Int): MenuAction {
        if (index !in 0..actions.size) throw IllegalArgumentException("Invalid index")
        return actions[index - 1]
    }

    fun setActions(actions: List<MenuAction>) {
        this.actions = actions
    }

    fun getActionsList(): List<MenuAction> = actions

}