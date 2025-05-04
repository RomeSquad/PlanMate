package org.example.di

import org.example.logic.usecase.InsertProjectUseCase
import org.example.logic.usecase.InsertUserUseCase
import org.example.logic.usecase.LoginUseCase
import org.example.logic.usecase.task.*
import org.koin.dsl.module

val logicModule = module {

    single { InsertProjectUseCase(get()) }
    single { InsertUserUseCase(get()) }
    single { LoginUseCase(get()) }
    single { CreateTaskUseCase(get()) }
    single { DeleteTaskUseCase(get()) }
    single { EditTaskUseCase(get()) }
    single { GetAllTasksUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
}