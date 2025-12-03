/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database

import androidx.room.testing.MigrationTestHelper
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

expect fun migrationTestHelper(fileName: String): MigrationTestHelper

class MigrationTest {
    private val tempFile =
        FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve("test-${Random.nextInt()}.db")

    @BeforeTest
    @IgnoreAndroidUnitTest
    @IgnoreNativeTest
    fun before() {
        FileSystem.SYSTEM.delete(tempFile)
        FileSystem.SYSTEM.delete("$tempFile.lck".toPath())
    }

    @AfterTest
    @IgnoreAndroidUnitTest
    @IgnoreNativeTest
    fun after() {
        FileSystem.SYSTEM.delete(tempFile)
        FileSystem.SYSTEM.delete("$tempFile.lck".toPath())
    }

    @Test
    @IgnoreAndroidUnitTest
    @IgnoreNativeTest
    fun migrate1To2() {
        val migrationTestHelper =
            migrationTestHelper(
                tempFile.toString(),
            )
        val newConnection = migrationTestHelper.createDatabase(1)
        newConnection.close()

        val migratedConnection =
            migrationTestHelper.runMigrationsAndValidate(2, listOf(MIGRATION_1_2))
        migratedConnection.close()
    }

    @Test
    @IgnoreAndroidUnitTest
    @IgnoreNativeTest
    fun migrate2To3() {
        val migrationTestHelper =
            migrationTestHelper(
                tempFile.toString(),
            )
        val newConnection = migrationTestHelper.createDatabase(2)
        newConnection.close()

        val migratedConnection =
            migrationTestHelper.runMigrationsAndValidate(3, listOf(MIGRATION_2_3))
        migratedConnection.close()
    }

    @Test
    @IgnoreAndroidUnitTest
    @IgnoreNativeTest
    fun migrate3To4() {
        val migrationTestHelper =
            migrationTestHelper(
                tempFile.toString(),
            )
        val newConnection = migrationTestHelper.createDatabase(3)
        newConnection.close()

        val migratedConnection =
            migrationTestHelper.runMigrationsAndValidate(4, listOf(MIGRATION_3_4))
        migratedConnection.close()
    }

    @Test
    @IgnoreAndroidUnitTest
    @IgnoreNativeTest
    fun migrate4To5() {
        val migrationTestHelper =
            migrationTestHelper(
                tempFile.toString(),
            )
        val newConnection = migrationTestHelper.createDatabase(4)
        newConnection.close()

        val migratedConnection =
            migrationTestHelper.runMigrationsAndValidate(5, listOf(MIGRATION_4_5))
        migratedConnection.close()
    }

    @Test
    @IgnoreAndroidUnitTest
    @IgnoreNativeTest
    fun migrate5To6() {
        val migrationTestHelper =
            migrationTestHelper(
                tempFile.toString(),
            )
        val newConnection = migrationTestHelper.createDatabase(5)
        newConnection.close()

        val migratedConnection =
            migrationTestHelper.runMigrationsAndValidate(6, listOf(MIGRATION_5_6))
        migratedConnection.close()
    }
}
