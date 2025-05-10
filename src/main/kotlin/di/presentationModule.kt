package org.example.di

import org.example.presentation.CLIMenu
import org.example.presentation.auithentication.LoginManagementUI
import org.example.presentation.auithentication.MainMenuUI
import org.example.presentation.history.ShowProjectHistoryUI
import org.example.presentation.history.ShowTaskHistoryUI
import org.example.presentation.project.*
import org.example.presentation.projectstates.*
import org.example.presentation.task.*
import org.example.presentation.user.admin.*
import org.example.presentation.user.mate.MateManagementUI
import org.example.presentation.utils.io.ConsoleInputReader
import org.example.presentation.utils.io.ConsoleWriter
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.koin.dsl.module
import presentation.App
import presentation.io.InputReader

val presentationModule = module {
    single<UiDisplayer> { ConsoleWriter() }
    single<InputReader> { ConsoleInputReader() }
    single<Menu> { Menu() }

    single {
        LoginManagementUI(
            get(),
            get()
        )
    }
    single {
        MainMenuUI(
            get(),
            get()
        )
    }
    single {
        AdminManagementUI(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        MateManagementUI(
            get()
        )
    }
    single {
        CreateUserUi(
            get()
        )
    }
    single {
        DeleteUserUi(
            get()
        )
    }
    single {
        EditUserUI(
            get(),
            get()
        )
    }
    single {
        ViewAllUserUI(
            get()
        )
    }

    single {
        ProjectManagementUI(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        CreateProjectUi(
            get(),
            get()
        )
    }
    single {
        GetProjectByIdUI(
            get()
        )
    }
    single {
        DeleteProjectUi(
            get()
        )
    }
    single {
        EditProjectUi(
            get()
        )
    }
    single {
        ListProjectUi(
            get()
        )
    }

    single {
        ProjectStateManagementUI(
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        AddStateToProjectUI(
            get()
        )
    }
    single {
        EditProjectStateUI(
            get()
        )
    }
    single {
        DeleteStateToProjectUI(
            get()
        )
    }
    single {
        GetAllStatesPerProjectUI(
            get()
        )
    }

    single {
        TaskManagementUI(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single {
        CreateTaskUI(
            get()
        )
    }
    single {
        DeleteTaskUI(
            get()
        )
    }
    single {
        EditTaskUI(
            get()
        )
    }
    single {
        GetAllTasksUI(
            get()
        )
    }
    single {
        GetTaskByIdUI(
            get()
        )
    }
    single {
        GetTasksByProjectIdUI(
            get()
        )
    }

    single {
        ShowTaskHistoryUI(
            get()
        )
    }
    single {
        ShowProjectHistoryUI(
            get()
        )
    }

    single {
        App(
            get(),
            get(),
            get(),
            get(),
        )
    }
    single {
        CLIMenu(
            get(),
            get(), 
        )
    }
}