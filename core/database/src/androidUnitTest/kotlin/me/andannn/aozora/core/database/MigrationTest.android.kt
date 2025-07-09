package me.andannn.aozora.core.database

import androidx.room.testing.MigrationTestHelper

actual fun migrationTestHelper(fileName: String): MigrationTestHelper {
    error("MigrationTestHelper is not available in Android unit tests. ")
}
