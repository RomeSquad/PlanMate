package org.example.di

import org.example.presentation.action.InsertProjectMenuAction
import org.example.presentation.menus.AdminMenu
import org.example.presentation.menus.MainMenu
import org.example.presentation.menus.MateMenu
import org.koin.dsl.module
import org.example.presentation.menus.Menu
import org.example.presentation.menus.MenuAction
import org.koin.core.qualifier.named
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
            InsertProjectMenuAction(get())
        )
    }

//    single(named("mateMenu")) {
//        listOf<MenuAction>(
//        )
//    }
//
//    single(named("adminMenu")) {
//        listOf<MenuAction>(
//        )
//    }


}