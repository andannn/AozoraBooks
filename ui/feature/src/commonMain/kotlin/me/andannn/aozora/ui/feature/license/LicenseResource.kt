/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.license

import kotlinx.serialization.json.Json
import me.andannn.aozora.core.domain.model.LibraryInfo
import me.andannn.aozora.ui.common.util.licenseJson

private val json =
    Json {
        ignoreUnknownKeys = true
    }

internal suspend fun getLicenseInfo(): List<LibraryInfo> = json.decodeFromString(licenseJson())
