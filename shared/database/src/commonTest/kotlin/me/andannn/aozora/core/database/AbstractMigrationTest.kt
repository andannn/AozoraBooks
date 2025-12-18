/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database

import androidx.room.testing.MigrationTestHelper
import kotlin.test.Test

abstract class AbstractMigrationTest {
    abstract val helper: MigrationTestHelper

    @Test
    fun migrate1To2(): Unit =
        helper.let { migrationTestHelper ->
            val newConnection = migrationTestHelper.createDatabase(1)
            newConnection.close()

            val migratedConnection =
                migrationTestHelper.runMigrationsAndValidate(2, listOf(MIGRATION_1_2))
            migratedConnection.close()
        }

    @Test
    fun migrate2To3(): Unit =
        helper.let { migrationTestHelper ->
            val newConnection = migrationTestHelper.createDatabase(2)
            newConnection.close()

            val migratedConnection =
                migrationTestHelper.runMigrationsAndValidate(3, listOf(MIGRATION_2_3))
            migratedConnection.close()
        }

    @Test
    fun migrate3To4(): Unit =
        helper.let { migrationTestHelper ->

            val newConnection = migrationTestHelper.createDatabase(3)
            newConnection.close()

            val migratedConnection =
                migrationTestHelper.runMigrationsAndValidate(4, listOf(MIGRATION_3_4))
            migratedConnection.close()
        }

    @Test
    fun migrate4To5(): Unit =
        helper.let { migrationTestHelper ->

            val newConnection = migrationTestHelper.createDatabase(4)
            newConnection.close()

            val migratedConnection =
                migrationTestHelper.runMigrationsAndValidate(5, listOf(MIGRATION_4_5))
            migratedConnection.close()
        }

    @Test
    fun migrate5To6(): Unit =
        helper.let { migrationTestHelper ->

            val newConnection = migrationTestHelper.createDatabase(5)
            newConnection.close()

            val migratedConnection =
                migrationTestHelper.runMigrationsAndValidate(6, listOf(MIGRATION_5_6))
            migratedConnection.close()
        }
}
