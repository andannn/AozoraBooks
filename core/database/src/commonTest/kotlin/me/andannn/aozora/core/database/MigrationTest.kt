package me.andannn.aozora.core.database

import androidx.room.testing.MigrationTestHelper
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

expect fun getMigrationTestHelper(fileName: String): MigrationTestHelper

class MigrationTest {
    private val tempFile =
        FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve("test-${Random.nextInt()}.db")

    @BeforeTest
    fun before() {
        FileSystem.SYSTEM.delete(tempFile)
        FileSystem.SYSTEM.delete("$tempFile.lck".toPath())
    }

    @AfterTest
    fun after() {
        FileSystem.SYSTEM.delete(tempFile)
        FileSystem.SYSTEM.delete("$tempFile.lck".toPath())
    }
//
//    @Test
//    fun migrate3To4() {
//        val migrationTestHelper =
//            getMigrationTestHelper(
//                tempFile.toString(),
//            )
//        val newConnection = migrationTestHelper.createDatabase(3)
//        newConnection.close()
//
//        val migratedConnection =
//            migrationTestHelper.runMigrationsAndValidate(4)
//        migratedConnection.close()
//    }
}
