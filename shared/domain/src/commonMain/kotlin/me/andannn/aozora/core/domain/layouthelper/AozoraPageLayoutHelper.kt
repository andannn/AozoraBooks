/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.layouthelper

import me.andannn.aozora.core.domain.model.AozoraPage
import me.andannn.aozora.core.domain.model.LayoutPage

interface AozoraPageLayoutHelper {
    fun AozoraPage.layout(): LayoutPage
}
