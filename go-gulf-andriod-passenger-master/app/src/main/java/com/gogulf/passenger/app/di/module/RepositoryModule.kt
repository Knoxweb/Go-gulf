package com.gogulf.passenger.app.di.module

import com.gogulf.passenger.app.data.repository.FirebaseRepository
import com.gogulf.passenger.app.data.repository.MainRepository
import org.koin.dsl.module


val repoModule = module {
    single {
        MainRepository(get())
    }
    single {
        FirebaseRepository()
    }
}