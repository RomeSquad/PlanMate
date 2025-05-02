package org.example.presentation.menus

import org.koin.java.KoinJavaComponent.getKoin
import presentation.App

open class Menu(
    open val actions: List<MenuAction>,
    val app: App
) {

    fun getAction(index: Int): MenuAction {
        if (index !in 0..actions.size) throw IllegalArgumentException("Invalid index")
        return actions[index - 1]
    }

    fun getActions(): List<MenuAction> = actions

    /*
     *  You can use koin to get the menu you need. Example:
     *  val adminMenu : AdminMenu = getKoin().get()
     *  changeMenu(adminMenu)
     */
    fun changeMenu(menu: Menu) {
        app.changeMenu(menu)
    }
}