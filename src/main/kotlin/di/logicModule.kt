package org.example.di

import org.example.logic.usecase.InsertProjectUseCase
import org.example.logic.usecase.InsertUserUseCase
import org.example.logic.usecase.LoginUseCase
import org.koin.dsl.module

val logicModule = module {

    single { InsertProjectUseCase(get()) }
    single { InsertUserUseCase(get()) }
    single { LoginUseCase(get()) }
}