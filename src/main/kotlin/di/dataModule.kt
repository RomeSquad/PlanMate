package org.example.di

import org.example.data.datasource.authentication.AuthDataSource
import org.example.data.datasource.authentication.CsvAuthDataSource
import org.example.data.datasource.project.CsvProjectDataSource
import org.example.data.datasource.project.ProjectDataSource
import org.example.data.repository.AuthRepositoryImpl
import org.example.data.repository.ProjectRepositoryImpl
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileReaderImpl
import org.example.data.utils.CsvFileWriter
import org.example.data.utils.CsvFileWriterImpl
import org.example.data.utils.FileValidator
import org.example.data.utils.Parser
import org.example.data.utils.ParserImpl
import org.example.logic.repository.AuthRepository
import org.example.logic.repository.ProjectRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single<File>(named("projectFile")) { File("project.csv") }
    single<File>(named("usersFile")) { File("users.csv") }

    single { FileValidator() }
    single<Parser> { ParserImpl() }

    single<CsvFileReader> { CsvFileReaderImpl(get(), get()) }
    single<CsvFileWriter> { CsvFileWriterImpl(get()) }

    single<ProjectDataSource> { CsvProjectDataSource(get(), get(), get(named("projectFile"))) }
    single<AuthDataSource> { CsvAuthDataSource(get(), get(), get(named("usersFile"))) }

    //TODO: add other data sources. Follow the same pattern as above


    single<ProjectRepository> { ProjectRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }



}