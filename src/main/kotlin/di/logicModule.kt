package org.example.di

import org.example.data.repository.ProjectRepositoryImpl
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.InsertProjectUseCase
import org.koin.dsl.module

val logicModule = module {

    single { InsertProjectUseCase(get()) }
}