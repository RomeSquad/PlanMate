package org.example.di

import org.example.data.datasource.project.CsvProjectDataSource
import org.example.data.datasource.project.ProjectDataSource
import org.example.data.utils.CsvFileReader
import org.example.data.utils.CsvFileReaderImpl
import org.example.data.utils.CsvFileWriter
import org.example.data.utils.CsvFileWriterImpl
import org.example.data.utils.FileValidator
import org.example.data.utils.Parser
import org.example.data.utils.ParserImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single(named("projectFile")) { File("project.csv") }
    single { FileValidator() }
    single<Parser> { ParserImpl() }

    single<CsvFileReader> { CsvFileReaderImpl(get(), get()) }
    single<CsvFileWriter> { CsvFileWriterImpl(get()) }

    single<ProjectDataSource> { CsvProjectDataSource(get(named("projectFile")), get(), get()) }

    //TODO: add other data sources. Follow the same pattern as above




}