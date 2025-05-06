package org.example

import org.example.di.dataModule
import org.example.di.logicModule
import org.example.di.presentationModule
import org.example.presentation.navigation.Route
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import presentation.navigation.MainUiController

fun main() {
    startKoin {
        modules(dataModule, logicModule, presentationModule)
    }

    val app: MainUiController = getKoin().get()
//    app.onNavigate(Route.LoginScreen)

}