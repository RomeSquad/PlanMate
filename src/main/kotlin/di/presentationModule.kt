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
            get(), // LoginUseCase
            get()  // MainMenuUI
        )
    }
    single {
        MainMenuUI(
            get(), // AdminManagementUI
            get()  // MateManagementUI
        )
    }
    single {
        AdminManagementUI(
            get(), // ProjectManagementUI
            get(), // CreateUserUi
            get(), // DeleteUserUi
            get(), // EditUserUI
            get()  // ViewAllUserUI
        )
    }
    single {
        MateManagementUI(
            get() // TaskManagementUI
        )
    }
    single {
        CreateUserUi(
            get() // CreateUserUseCase
        )
    }
    single {
        DeleteUserUi(
            get() // DeleteUserUseCase
        )
    }
    single {
        EditUserUI(
            get(), // GetUserByUsernameUseCase
            get()  // EditUserUseCase
        )
    }
    single {
        ViewAllUserUI(
            get() // GetAllUsersUseCase
        )
    }

    single {
        ProjectManagementUI(
            get(), // CreateProjectUi
            get(), // DeleteProjectUi
            get(), // EditProjectUi
            get(), // ListProjectUi
            get(), // TaskManagementUI
            get(), // ProjectStateManagementUI
            get(),  // SaveAllProjectUseCase
            get() // GetProjectByIdUI
        )
    }
    single {
        CreateProjectUi(
            get(), // InsertProjectUseCase
            get()  // DefaultProjectStateUseCase
        )
    }
    single {
        GetProjectByIdUI(
            get() // GetProjectByIdUseCase
        )
    }
    single {
        DeleteProjectUi(
            get() // DeleteProjectByIdUseCase
        )
    }
    single {
        EditProjectUi(
            get() // EditProjectUseCase
        )
    }
    single {
        ListProjectUi(
            get() // GetAllProjectsUseCase
        )
    }

    single {
        ProjectStateManagementUI(
            get(), // AddStateToProjectUI
            get(), // EditProjectStateUI
            get(), // DeleteStateToProjectUI
            get()  // GetAllStatesPerProjectUI
        )
    }
    single {
        AddStateToProjectUI(
            get() // AddStateToProjectUseCase
        )
    }
    single {
        EditProjectStateUI(
            get() // EditProjectStateUseCase
        )
    }
    single {
        DeleteStateToProjectUI(
            get() // DeleteStateToProjectUseCase
        )
    }
    single {
        GetAllStatesPerProjectUI(
            get() // GetAllStatesPerProjectUseCase
        )
    }

    single {
        TaskManagementUI(
            get(), // CreateTaskUI
            get(), // DeleteTaskUI
            get(), // EditTaskUI
            get(), // GetAllTasksUI
            get(), // GetTaskByIdUI
            get()  // GetTasksByProjectIdUI
        )
    }
    single {
        CreateTaskUI(
            get() // CreateTaskUseCase
        )
    }
    single {
        DeleteTaskUI(
            get() // DeleteTaskUseCase
        )
    }
    single {
        EditTaskUI(
            get() // EditTaskUseCase
        )
    }
    single {
        GetAllTasksUI(
            get() // GetAllTasksUseCase
        )
    }
    single {
        GetTaskByIdUI(
            get() // GetTaskByIdUseCase
        )
    }
    single {
        GetTasksByProjectIdUI(
            get() // GetTasksByProjectIdUseCase
        )
    }

    single {
        ShowTaskHistoryUI(
            get() // TaskHistoryUseCase or similar
        )
    }
    single {
        ShowProjectHistoryUI(
            get() // ProjectHistoryUseCase or similar
        )
    }

    single {
        App(
            get(),
            get(), // MainMenuUI
            get(), // LoginManagementUI
            get(), // ProjectManagementUI
        )
    }
    single {
        CLIMenu(
            get(), // App
            get(), // MainMenuUI
        )
    }
}