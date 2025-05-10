package org.example.di

import org.example.presentation.CLIMenu
import org.example.presentation.auth.LoginManagementUI
import org.example.presentation.auth.MainMenuUI
import org.example.presentation.io.ConsoleWriter
import org.example.presentation.io.InputReader
import org.example.presentation.io.UiDisplayer
import org.example.presentation.menus.Menu
import org.example.presentation.user.admin.*
import org.example.presentation.user.mate.MateManagementUI
import org.koin.dsl.module
import presentation.App
import presentation.io.ConsoleInputReader


val presentationModule = module {

    single<InputReader> { ConsoleInputReader() }
    single<UiDisplayer> { ConsoleWriter() }

    single<Menu> { Menu() }
    single { "" }

    single {
        LoginManagementUI(
            get(), get(),
        )
    }
    single { MainMenuUI(get(), get()) } // AdminManagementUI, MateManagementUI, UiDisplayer, InputReader
    single {
        AdminManagementUI(
            get(),
            get(),
            get(),
            get(),
//            get(),
        )
    } // ProjectManagementUI, CreateUserUi, DeleteUserUi, EditUserUI, ViewAllUserUI, UiDisplayer, InputReader
    single { MateManagementUI() } // TaskManagementUI, UiDisplayer, InputReader
    single { CreateUserUi(get()) } // CreateUserUseCase, Menu, UiDisplayer
    single { DeleteUserUi(get()) } // DeleteUserUseCase, Menu, UiDisplayer
    single { EditUserUI(get(), get()) } // GetUserByUsernameUseCase, EditUserUseCase, Menu, UiDisplayer
    single { ViewAllUserUI(get()) } // GetAllUsersUseCase, Menu, UiDisplayer

    single { App(get(), get(), get(), get()) } // UiDisplayer, InputReader, Menu, LoginManagementUI
    single { CLIMenu(get(), get()) }

}