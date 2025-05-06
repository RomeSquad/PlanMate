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
    val mongo: MongoCollection<User> = getKoin().get(named("users-collection"))
    val app: App = getKoin().get()
    runBlocking {
        val result = mongo.insertOne(
            User(
                userId = 1,
                username = "amr",
                password = "c24a542f884e144451f9063b79e7994e",
                userRole = UserRole.MATE
            )
        )
        println(result.wasAcknowledged())

        app.start()
    }
}