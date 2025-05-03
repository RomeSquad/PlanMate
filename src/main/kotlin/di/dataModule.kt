package org.example.di

import org.example.data.datasource.authentication.AuthDataSource
import org.example.data.datasource.authentication.CsvAuthDataSource
import org.example.data.datasource.project.CsvProjectDataSource
import org.example.data.datasource.project.ProjectDataSource
import org.example.data.datasource.task.CsvTaskDataSource
import org.example.data.repository.AuthRepositoryImpl
import org.example.data.repository.ProjectRepositoryImpl
import org.example.data.repository.TaskRepositoryImpl
import org.example.data.utils.*
import org.example.logic.repository.AuthRepository
import org.example.logic.repository.ProjectRepository
import org.example.logic.repository.TaskRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single<File>(named("projectFile")) { File("project.csv") }
    single<File>(named("usersFile")) { File("users.csv") }
    single<File>(named("taskFile")) { File("task.csv") }

    single { FileValidator() }
    single<Parser> { ParserImpl() }

    single<CsvFileReader> { CsvFileReaderImpl(get(), get()) }
    single<CsvFileWriter> { CsvFileWriterImpl(get()) }

    single<ProjectDataSource> { CsvProjectDataSource(get(), get(), get(named("projectFile"))) }
    single<AuthDataSource> { CsvAuthDataSource(get(), get(), get(named("usersFile"))) }
    single<CsvTaskDataSource> { CsvTaskDataSource(get(), get(), get(named("taskFile"))) }

    //TODO: add other data sources. Follow the same pattern as above


    single<ProjectRepository> { ProjectRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
}