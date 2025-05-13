package org.example.di

import org.example.presentation.CLIMenu
import org.example.presentation.auithentication.LoginManagementUI
import org.example.presentation.auithentication.MainMenuUI
import org.example.presentation.history.ShowHistoryManagementUI
import org.example.presentation.history.ShowProjectHistoryUI
import org.example.presentation.history.ShowTaskHistoryUI
import org.example.presentation.project.*
import org.example.presentation.projectstates.*
import org.example.presentation.task.*
import org.example.presentation.user.admin.*
import org.example.presentation.user.mate.MateManagementUI
import org.example.presentation.utils.io.ConsoleInputReader
import org.example.presentation.utils.io.ConsoleWriter
import org.example.presentation.utils.io.InputReader
import org.example.presentation.utils.io.UiDisplayer
import org.example.presentation.utils.menus.Menu
import org.koin.dsl.module
import presentation.App

val presentationModule = module {
    single<UiDisplayer> { ConsoleWriter() }
    single<InputReader> { ConsoleInputReader() }
    single<Menu> { Menu() }

    single {
        LoginManagementUI(
            get(),
            get(),
            get(),
        )
    }
    single {
        MainMenuUI(
            get(),
            get(),
            get(),
        )
    }
    single {
        AdminManagementUI(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
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
            get(),
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
        )
    }
    single {
        CreateProjectUi(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    single {
        DeleteProjectUi(
            get(),
            get(),
        )
    }
    single {
        EditProjectUi(
            get(),
            get(),
            get(),
            get(),
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
            get(),
            get(),
        )
    }
    single {
        AddStateToProjectUI(
            get(),
            get(),
            get(),
        )
    }
    single {
        AddTaskStateToProjectUI(
            get(),
            get()
        )
    }
    single {
        EditProjectStateUI(
            get(),
            get(),
        )
    }
    single {
        DeleteStateToProjectUI(
            get(),
            get(),
            get(),
        )
    }
    single {
        GetAllStatesPerProjectUI(
            get(),
            get(),
        )
    }

    single {
        TaskManagementUI(
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    single {
        CreateTaskUI(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    single {
        DeleteTaskUI(
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    single {
        EditTaskUI(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
    single {
        SwimlanesView(
            get(),
            get(),
        )
    }
    single {
        ShowTaskHistoryUI(
            get(),
            get(),
            get(),
        )
    }
    single {
        ShowProjectHistoryUI(
            get(),
            get(),
        )
    }
    single {
        ShowHistoryManagementUI(
            get(),
            get(),
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