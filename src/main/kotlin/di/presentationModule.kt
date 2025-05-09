package org.example.di

import org.example.presentation.CLIMenu
import org.example.presentation.auithentication.LoginManagementUI
import org.example.presentation.auithentication.MainMenuUI
import org.example.presentation.project.*
import org.example.presentation.projectstates.*
import org.example.presentation.task.*
import org.example.presentation.user.admin.*
import org.example.presentation.user.mate.MateManagementUI
import org.example.presentation.utils.io.ConsoleWriter
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.koin.dsl.module
import presentation.App
import org.example.presentation.utils.io.ConsoleInputReader
import presentation.io.InputReader


val presentationModule = module {
    single<UiDisplayer> { ConsoleWriter() }
    single<InputReader> { ConsoleInputReader() }
    single<Menu> { Menu() }
    single { "" }

    single {
        LoginManagementUI(
            get(), get(),
        )
    } // LoginUseCase, MainMenuUI, UiDisplayer, InputReader
    single { MainMenuUI(get(), get()) } // AdminManagementUI, MateManagementUI, UiDisplayer, InputReader
    single {
        AdminManagementUI(
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    } // ProjectManagementUI, CreateUserUi, DeleteUserUi, EditUserUI, ViewAllUserUI, UiDisplayer, InputReader
    single { MateManagementUI(get()) } // TaskManagementUI, UiDisplayer, InputReader
    single { CreateUserUi(get()) } // CreateUserUseCase, Menu, UiDisplayer
    single { DeleteUserUi(get()) } // DeleteUserUseCase, Menu, UiDisplayer
    single { EditUserUI(get(), get()) } // GetUserByUsernameUseCase, EditUserUseCase, Menu, UiDisplayer
    single { ViewAllUserUI(get()) } // GetAllUsersUseCase, Menu, UiDisplayer

    single {
        ProjectManagementUI(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    } // CreateProjectUi, DeleteProjectUi, EditProjectUi, ListProjectUi, TaskManagementUI, ProjectStateManagementUI, SaveAllProjectUseCase, Menu, UiDisplayer
    single {
        CreateProjectUi(
            get(),
            get()
        )
    } // InsertProjectUseCase, DefaultProjectStateUseCase, Menu, UiDisplayer
    single { GetProjectByIdUI(get()) } // GetProjectByIdUseCase, Menu, UiDisplayer
    single { DeleteProjectUi(get()) } // DeleteProjectByIdUseCase, Menu, UiDisplayer
    single { EditProjectUi(get()) } // EditProjectUseCase
    single { ListProjectUi(get()) } // GetAllProjectsUseCase, Menu, UiDisplayer

    single {
        ProjectStateManagementUI(
            get(),
            get(),
            get(),
            get(),
        )
    } // AddStateToProjectUI, EditProjectStateUI, DeleteStateToProjectUI, GetAllStatesPerProjectUI, Menu, UiDisplayer
    single { AddStateToProjectUI(get()) } // AddStateToProjectUseCase, Menu, UiDisplayer
    single { EditProjectStateUI(get()) } // EditProjectStateUseCase, Menu, UiDisplayer
    single { DeleteStateToProjectUI(get()) } // DeleteStateToProjectUseCase, Menu, UiDisplayer
    single { GetAllStatesPerProjectUI(get()) } //GetAllStatesPerProjectUseCase, Menu, UiDisplayer
    single {
        TaskManagementUI(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    } // CreateTaskUI, DeleteTaskUI, EditTaskUI, GetAllTasksUI, GetTaskByIdUI, GetTasksByProjectIdUI, Menu, UiDisplayer
    single { CreateTaskUI(get(), get()) } // CreateTaskUseCase, Menu, UiDisplayer, InputReader
    single { DeleteTaskUI(get()) } // DeleteTaskUseCase, Menu, UiDisplayer
    single { EditTaskUI(get()) } // EditTaskUseCase, Menu, UiDisplayer
    single { GetAllTasksUI(get()) } // GetAllTasksUseCase, Menu, UiDisplayer
    single { GetTaskByIdUI(get()) } // GetTaskByIdUseCase, Menu, UiDisplayer
    single { GetTasksByProjectIdUI(get()) } // GetTasksByProjectIdUseCase, Menu, UiDisplayer

    single { App(get(), get(), get(), get()) } // UiDisplayer, InputReader, Menu, LoginManagementUI
    single { CLIMenu(get(), get()) }
}