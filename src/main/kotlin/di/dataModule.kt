package org.example.di

import CsvProjectStateDataSource
import ProjectStateRepositoryImpl
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.example.data.datasource.authentication.AuthDataSource
import org.example.data.datasource.authentication.MongoAuthDataSource
import org.example.data.datasource.project.CsvProjectDataSource
import org.example.data.datasource.project.ProjectDataSource
import org.example.data.datasource.task.MongoTaskDataSource
import org.example.data.datasource.state.ProjectStateDataSource
import org.example.data.datasource.task.CsvTaskDataSource
import org.example.data.datasource.task.TaskDataSource
import org.example.data.repository.AuthRepositoryImpl
import org.example.data.repository.ProjectRepositoryImpl
import org.example.data.repository.TaskRepositoryImpl
import org.example.data.utils.*
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import org.example.logic.entity.auth.User
import org.example.logic.repository.AuthRepository
import org.example.logic.repository.ProjectRepository
import org.example.logic.repository.ProjectStateRepository
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
    single<AuthDataSource> { MongoAuthDataSource(get(named("users-collection"))) }
    single<TaskDataSource> { MongoTaskDataSource(get(named("tasks-collection"))) }
    single<ProjectStateDataSource> { CsvProjectStateDataSource() }

    //TODO: add other data sources. Follow the same pattern as above


    single<ProjectRepository> { ProjectRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<ProjectStateRepository> { ProjectStateRepositoryImpl(get()) }

    single<MongoDatabase> {

        val uri = "mongodb+srv://rome:rome@plan-mate.rxaopvb.mongodb.net/?retryWrites=true&w=majority&appName=plan-mate"

        val mongoClient: MongoClient = MongoClient.create(uri)
        mongoClient.getDatabase("plan-mate")
    }
    single<MongoCollection<User>>(named("users-collection")) {
        get<MongoDatabase>().getCollection<User>("users")
    }
    single<MongoCollection<Project>>(named("projects-collection")) {
        get<MongoDatabase>().getCollection<Project>("projects")
    }
    single<MongoCollection<Task>>(named("tasks-collection")) {
        get<MongoDatabase>().getCollection<Task>("tasks")
    }
    single<MongoCollection<ProjectState>>(named("states-collection")){
        get<MongoDatabase>().getCollection<ProjectState>("states")
    }
}