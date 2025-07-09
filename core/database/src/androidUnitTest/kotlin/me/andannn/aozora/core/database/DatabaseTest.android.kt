package me.andannn.aozora.core.database

import androidx.room.RoomDatabase

actual val inMemoryDatabaseBuilder: RoomDatabase.Builder<AozoraDataBase>
    get() = error("In-memory database is not supported on Android unit tests.")

actual typealias IgnoreAndroidUnitTest = org.junit.Ignore

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
actual annotation class IgnoreNativeTest actual constructor()
