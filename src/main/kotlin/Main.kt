package org.example

import org.example.di.dataModule
import org.example.di.logicModule
import org.example.di.presentationModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.App

fun main() {
    startKoin {
        modules(dataModule, logicModule, presentationModule)
    }

    val app: App = getKoin().get()
    app.start()

}