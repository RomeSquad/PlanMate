package org.example.di

import logic.usecase.project.EditProjectUseCase
import org.example.logic.usecase.auth.*
import org.example.logic.usecase.history.AddChangeHistoryUseCase
import org.example.logic.usecase.history.ShowProjectHistoryUseCase
import org.example.logic.usecase.history.ShowTaskHistoryUseCase
import org.example.logic.usecase.project.*
import org.example.logic.usecase.state.*
import org.example.logic.usecase.task.*
import org.koin.dsl.module

val logicModule = module {

    //region Projects
    single { InsertProjectUseCase(get()) }
    single { GetProjectByIdUseCase(get()) }
    single { SaveAllProjectUseCase(get()) }
    single { EditProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }
    single { DeleteProjectByIdUseCase(get()) }
    //endregion

    //region User
    single { InsertUserUseCase(get()) }
    single { LoginUseCase(get()) }
    single { GetAllUsersUseCase(get()) }
    single { DeleteUserUseCase(get()) }
    single { EditUserUseCase(get()) }
    single { GetUserByUsernameUseCase(get()) }
    //endregion

    //region Task
    single { CreateTaskUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
    single { EditTaskUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }
    //endregion

    //region State
    single { AddCustomProjectStateUseCase(get()) }
    single { AddProjectStatesUseCase(get()) }
    single { DefaultProjectStateUseCase(get()) }
    single { DeleteProjectStatesUseCase(get()) }
    single { EditProjectStateUseCase(get()) }
    single { GetAllProjectStatesUseCase(get()) }
    single { GetStateByTaskIdUseCase(get()) }
    //endregion

    //region Change History
    single { AddChangeHistoryUseCase(get()) }
    single { ShowTaskHistoryUseCase(get()) }
    single { ShowProjectHistoryUseCase(get()) }
    //endregion
}