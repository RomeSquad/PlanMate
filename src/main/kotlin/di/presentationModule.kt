package org.example.di

import org.example.presentation.action.InsertProjectMenuAction
import org.example.presentation.action.InsertUserMenuAction
import org.example.presentation.action.LoginMenuAction
import org.example.presentation.action.MateMenuAction
import org.example.presentation.menus.MenuAction
import org.koin.core.qualifier.named
import org.koin.dsl.module
import presentation.io.ConsoleInputReader
import presentation.io.ConsoleWriter
import presentation.io.InputReader
import presentation.io.UiDisplayer
import presentation.navigation.MainUiController
import presentation.navigation.NavigationController


val presentationModule = module {

    single<InputReader> { ConsoleInputReader() }
    single<UiDisplayer> { ConsoleWriter() }

    single { MainUiController(get(), get(),get(),get(),get()) }

    single {
        NavigationController()
    }
    single {
        MateMenuAction(get(), get(),get(),get(),get())
    }


    single(named("mainMenu")) {
        listOf<MenuAction>(
            InsertProjectMenuAction(get(),get(),get(),get())
        )
    }

    single(named("mateMenu")) {
        listOf<MenuAction>(
            LoginMenuAction(get(),get(),get())
        )
    }

    single(named("adminMenu")) {
        listOf<MenuAction>(
            InsertUserMenuAction(get(),get(),get(),get()),
            LoginMenuAction(get(),get(),get())
        )
    }


}