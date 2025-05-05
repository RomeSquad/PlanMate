package org.example.di

import org.example.presentation.action.ChooseRoleUseCase
import org.example.presentation.action.InsertProjectMenuAction
import org.koin.dsl.module
import org.example.presentation.menus.Menu
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

    single<Menu> { Menu() }

//    single(named("mainMenuActions")) {
//        listOf<MenuAction>(
//            ChooseRoleUseCase(menu = get())
//        )
//    }


    single(named("mateMenuActions")) {
        listOf<MenuAction>(
            LoginMenuAction(get())
        )
    }

    single(named("adminMenuActions")) {
        listOf<MenuAction>(
            InsertProjectMenuAction(projectUseCase = get(), menu = get()),
            LoginMenuAction(get())
        )
    }



    single { App(get(), get(), get()) }
}