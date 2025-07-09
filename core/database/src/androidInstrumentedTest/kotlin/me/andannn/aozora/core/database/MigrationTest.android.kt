package me.andannn.aozora.core.database

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File

actual fun migrationTestHelper(fileName: String) =
    MigrationTestHelper(
        instrumentation = InstrumentationRegistry.getInstrumentation(),
        file = File(fileName),
        driver = AndroidSQLiteDriver(),
        databaseClass = AozoraDataBase::class,
    )
