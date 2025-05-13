package org.example.di

import ProjectStateRepositoryImpl
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.datasource.authentication.dto.UserDto
import data.datasource.projectState.ProjectStateDataSource
import org.bson.UuidRepresentation
import org.example.data.datasource.authentication.AuthDataSource
import org.example.data.datasource.authentication.MongoAuthDataSource
import org.example.data.datasource.changelog.ChangeHistoryDataSource
import org.example.data.datasource.changelog.MongoChangeHistoryDataSource
import org.example.data.datasource.project.MongoProjectDataSource
import org.example.data.datasource.project.ProjectDataSource
import org.example.data.datasource.projectState.MongoProjectStateDataSource
import org.example.data.datasource.task.MongoTaskDataSource
import org.example.data.datasource.task.TaskDataSource
import org.example.data.repository.AuthRepositoryImpl
import org.example.data.repository.ChangeHistoryRepositoryImpl
import org.example.data.repository.ProjectRepositoryImpl
import org.example.data.repository.TaskRepositoryImpl
import org.example.data.utils.*
import org.example.logic.entity.ChangeHistory
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import org.example.logic.entity.auth.User
import org.example.logic.repository.*
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

    single<AuthDataSource> { MongoAuthDataSource(get(named("users-collection"))) }
    single<TaskDataSource> { MongoTaskDataSource(get(named("tasks-collection"))) }
    single<ChangeHistoryDataSource> { MongoChangeHistoryDataSource(get(named("change-history-collection"))) }
    single<ProjectDataSource> { MongoProjectDataSource(get(named("projects-collection"))) }
    single<ProjectStateDataSource> { MongoProjectStateDataSource(get(named("states-collection"))) }


    //TODO: add other data sources. Follow the same pattern as above


    single<ProjectRepository> { ProjectRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<ProjectStateRepository> { ProjectStateRepositoryImpl(get()) }
    single<ChangeHistoryRepository> { ChangeHistoryRepositoryImpl(get()) }

    single<MongoDatabase> {
        val uri =
            "mongodb+srv://rome:rome@plan-mate.rxaopvb.mongodb.net/?retryWrites=true&w=majority&appName=plan-mate"

        val connectionString = ConnectionString(uri)

        val settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .build()

        val client = MongoClient.create(settings)
        client.getDatabase("plan-mate")
    }
    single<MongoCollection<UserDto>>(named("users-collection")) {
        get<MongoDatabase>().getCollection<UserDto>("users")
    }
    single<MongoCollection<Project>>(named("projects-collection")) {
        get<MongoDatabase>().getCollection<Project>("projects")
    }
    single<MongoCollection<Task>>(named("tasks-collection")) {
        get<MongoDatabase>().getCollection<Task>("tasks")
    }
    single<MongoCollection<ProjectState>>(named("states-collection")) {
        get<MongoDatabase>().getCollection<ProjectState>("states")
    }
    single<MongoCollection<ChangeHistory>>(named("change-history-collection")) {
        get<MongoDatabase>().getCollection<ChangeHistory>("change-history")
    }
}