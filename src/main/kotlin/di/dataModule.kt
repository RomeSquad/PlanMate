package org.example.di

import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single { File("") }

}