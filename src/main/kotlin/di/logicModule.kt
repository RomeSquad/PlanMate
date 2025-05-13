package org.example.di

import logic.usecase.project.EditProjectUseCase
import logic.usecase.validator.UserCredentialsValidator
import logic.usecase.validator.UserCredentialsValidatorImpl
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
    single { InsertProjectUseCase(get(), get()) }
    single { GetProjectByIdUseCase(get()) }
    single { SaveAllProjectUseCase(get()) }
    single { EditProjectUseCase(get()) }
    single { GetAllProjectsUseCase(get()) }
    single { DeleteProjectByIdUseCase(get()) }
    single { ValidationProject() }
    //endregion

    //region User
    single<UserCredentialsValidator> { UserCredentialsValidatorImpl() }
    single { InsertUserUseCase(get(),get()) }
    single { LoginUseCase(get()) }
    single { GetAllUsersUseCase(get()) }
    single { DeleteUserUseCase(get()) }
    single { EditUserUseCase(get()) }
    single { GetUserByUsernameUseCase(get()) }
    single { GetCurrentUserUseCase(get()) }
    //endregion

    //region Task
    single { CreateTaskUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
    single { EditTaskUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { GetTasksByProjectIdUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    //endregion

    //region State
    single { AddCustomProjectStateUseCase(get()) }
    single { AddProjectStatesUseCase(get()) }
    single { DefaultProjectStateUseCase(get()) }
    single { DeleteProjectStatesUseCase(get()) }
    single { EditProjectStateUseCase(get()) }
    single { GetAllProjectStatesUseCase(get()) }
    single { GetStateByTaskIdUseCase(get()) }
    single { AddTaskStateToProjectUseCase(get(), get(), get()) }
    //endregion

    //region Change History
    single { AddChangeHistoryUseCase(get()) }
    single { ShowTaskHistoryUseCase(get()) }
    single { ShowProjectHistoryUseCase(get()) }
    //endregion
}