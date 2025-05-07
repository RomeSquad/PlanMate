package org.example

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.runBlocking
import org.example.di.dataModule
import org.example.di.logicModule
import org.example.di.presentationModule
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin
import presentation.App

fun main() {
    startKoin {
        modules(dataModule, logicModule, presentationModule)
    }

    val app: App = getKoin().get()
    runBlocking {
        app.start()
    }
}