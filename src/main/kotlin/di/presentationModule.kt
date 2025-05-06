package org.example.di

import org.example.presentation.action.InsertProjectMenuAction
import org.example.presentation.action.InsertUserMenuAction
import org.example.presentation.action.LoginMenuAction
import org.example.presentation.menus.MenuAction
import org.koin.core.qualifier.named
import org.koin.dsl.module
import presentation.App
import presentation.io.ConsoleInputReader
import presentation.io.ConsoleWriter
import presentation.io.InputReader
import presentation.io.UiDisplayer


val presentationModule = module {

    single<InputReader> { ConsoleInputReader() }
    single<UiDisplayer> { ConsoleWriter() }

    single { App(get(), get()) }


    single(named("mainMenu")) {
        listOf<MenuAction>(
            InsertUserMenuAction(get()),
            LoginMenuAction(get()),
            InsertProjectMenuAction(get())
        )
    }

    single(named("mateMenu")) {
        listOf<MenuAction>(
            LoginMenuAction(get())
        )
    }

    single(named("adminMenu")) {
        listOf<MenuAction>(
            InsertUserMenuAction(get()),
            LoginMenuAction(get())
        )
    }


}