/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.database

import androidx.room.testing.MigrationTestHelper

actual fun migrationTestHelper(fileName: String): MigrationTestHelper {
    error("MigrationTestHelper is not available in Android unit tests. ")
}
