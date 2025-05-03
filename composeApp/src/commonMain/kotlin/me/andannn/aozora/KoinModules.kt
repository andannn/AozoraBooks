package me.andannn.aozora

import com.andannn.aozora.core.service.di.serviceModule
import me.andannn.aozora.core.data.di.dataModule
import org.koin.core.module.Module

val modules: List<Module> =
    listOf(
        serviceModule,
        dataModule,
    )
