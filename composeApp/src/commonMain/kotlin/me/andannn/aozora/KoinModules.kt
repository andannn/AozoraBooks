package me.andannn.aozora

import com.andannn.aozora.core.service.di.serviceModule
import org.koin.core.module.Module

val modules: List<Module> =
    listOf(
        serviceModule,
    )
