package org.example.di

import org.example.presentation.menus.AdminMenu
import org.example.presentation.menus.MainMenu
import org.example.presentation.menus.MateMenu
import org.koin.dsl.module
import org.example.presentation.menus.Menu
import org.example.presentation.menus.MenuAction
import presentation.App
import presentation.io.ConsoleInputReader
import presentation.io.ConsoleWriter
import presentation.io.InputReader
import presentation.io.UiDisplayer


val presentationModule = module {

    single<InputReader> { ConsoleInputReader() }
    single<UiDisplayer> { ConsoleWriter() }

    single { App(get(), get()) }

    // region menu actions
//    single<MenuAction>{ DeleteThisFileMenuAction(get()) }
    // endregion

    single<Menu> {
        MainMenu(
            listOf( // TODO: Add menu actions here
            ), get()
        )
    }

    single<Menu> {
        MateMenu(
            listOf( // TODO: Add menu actions here

            ), get()
        )
    }
    single<Menu> {
        AdminMenu(
            listOf( // TODO: Add menu actions here

            ), get()
        )
    }


}