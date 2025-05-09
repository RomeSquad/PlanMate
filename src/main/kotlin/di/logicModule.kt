package org.example.di

import logic.usecase.project.EditProjectUseCase
import org.example.logic.usecase.auth.InsertUserUseCase
import org.example.logic.usecase.auth.LoginUseCase
import org.example.logic.usecase.history.AddChangeHistoryUseCase
import org.example.logic.usecase.history.ShowProjectHistoryUseCase
import org.example.logic.usecase.history.ShowTaskHistoryUseCase
import org.example.logic.usecase.project.GetProjectByIdUseCase
import org.example.logic.usecase.project.InsertProjectUseCase
import org.example.logic.usecase.project.SaveAllProjectUseCase
import org.example.logic.usecase.state.AddCustomStateUseCase
import org.example.logic.usecase.state.AddStatesUseCase
import org.example.logic.usecase.state.DefaultProjectStateUseCase
import org.example.logic.usecase.state.DeleteStatesUseCase
import org.example.logic.usecase.state.EditStateUseCase
import org.example.logic.usecase.state.GetAllStatesUseCase
import org.example.logic.usecase.task.*
import org.koin.dsl.module

val logicModule = module {

    single { InsertProjectUseCase(get()) }
    single { GetProjectByIdUseCase(get()) }
    single { SaveAllProjectUseCase(get()) }
    single { EditProjectUseCase(get()) }

    single { InsertUserUseCase(get()) }
    single { LoginUseCase(get()) }


    single { CreateTaskUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
    single { EditTaskUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }

    single { AddCustomStateUseCase(get()) }
    single{ AddStatesUseCase(get()) }
    single { DefaultProjectStateUseCase(get()) }
    single { DeleteProjectStatesUseCase(get()) }
    single { EditProjectStateUseCase(get()) }
    single { GetAllProjectStatesUseCase(get()) }
    single { AddChangeHistoryUseCase(get()) }
    single { ShowTaskHistoryUseCase(get()) }
    single { ShowProjectHistoryUseCase(get()) }
}