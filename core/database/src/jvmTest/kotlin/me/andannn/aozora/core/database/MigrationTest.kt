/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import okio.FileSystem
import okio.Path.Companion.toPath
import java.io.File
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class MigrationTest {
    private val tempFile =
        FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve("test-${Random.nextInt()}.db")

    private fun getMigrationTestHelper(fileName: String) =
        MigrationTestHelper(
            schemaDirectoryPath = File("schemas").toPath(),
            driver = BundledSQLiteDriver(),
            databaseClass = AozoraDataBase::class,
            databasePath = File(fileName).toPath(),
        )

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

    @Test
    fun migrate1To2() {
        val migrationTestHelper =
            getMigrationTestHelper(
                tempFile.toString(),
            )
        val newConnection = migrationTestHelper.createDatabase(1)
        newConnection.close()

        val migratedConnection =
            migrationTestHelper.runMigrationsAndValidate(2, listOf(MIGRATION_1_2))
        migratedConnection.close()
    }

    @Test
    fun migrate2To3() {
        val migrationTestHelper =
            getMigrationTestHelper(
                tempFile.toString(),
            )
        val newConnection = migrationTestHelper.createDatabase(2)
        newConnection.close()

        val migratedConnection =
            migrationTestHelper.runMigrationsAndValidate(3, listOf(MIGRATION_2_3))
        migratedConnection.close()
    }

    @Test
    fun migrate3To4() {
        val migrationTestHelper =
            getMigrationTestHelper(
                tempFile.toString(),
            )
        val newConnection = migrationTestHelper.createDatabase(3)
        newConnection.close()

        val migratedConnection =
            migrationTestHelper.runMigrationsAndValidate(4, listOf(MIGRATION_3_4))
        migratedConnection.close()
    }

    @Test
    fun migrate4To5() {
        val migrationTestHelper =
            getMigrationTestHelper(
                tempFile.toString(),
            )
        val newConnection = migrationTestHelper.createDatabase(4)
        newConnection.close()

        val migratedConnection =
            migrationTestHelper.runMigrationsAndValidate(5, listOf(MIGRATION_4_5))
        migratedConnection.close()
    }
}
