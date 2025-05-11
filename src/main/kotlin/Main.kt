package org.example

import kotlinx.coroutines.runBlocking
import logic.request.auth.CreateUserRequest
import org.example.data.datasource.authentication.AuthDataSource
import org.example.di.dataModule
import org.example.di.logicModule
import org.example.di.presentationModule
import org.example.logic.entity.auth.UserRole
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.inject
import presentation.App

fun main() {
    startKoin {
        modules(dataModule, logicModule, presentationModule)
    }
//    val mongo :AuthDataSource by inject(AuthDataSource::class.java)
//    runBlocking {
//        mongo.insertUser(request = CreateUserRequest(username = "admin2", password = "admin", userRole = UserRole.ADMIN))
//    }
    val app: App by inject(App::class.java)
    runBlocking {
        app.start()
    }
}