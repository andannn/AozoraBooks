package me.andannn.aozora.core.database

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

actual fun migrationTestHelper(fileName: String) =
    MigrationTestHelper(
        // absolute path is required for native tests
        schemaDirectoryPath = "<path of project>/core/database/schemas".also { error("replace to real absolute path.") },
        driver = BundledSQLiteDriver(),
        databaseClass = AozoraDataBase::class,
        fileName = fileName,
    )
