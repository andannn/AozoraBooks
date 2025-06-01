package me.andannn.aozora.syncer.di

import me.andannn.aozora.syncer.AozoraDBSyncer
import me.andannn.aozora.syncer.internal.AozoraDBSyncerImpl
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

val syncerModule: Module = module {
    singleOf(::AozoraDBSyncerImpl).bind(AozoraDBSyncer::class)
}
