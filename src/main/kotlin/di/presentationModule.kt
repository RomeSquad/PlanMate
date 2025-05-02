package org.example.di

import org.koin.dsl.module
import presentation.Menu

val presentationModule = module {

    single {
        Menu(
            listOf(
                TODO("Add your menu actions here")
            )
        )
    }
}