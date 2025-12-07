/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.layouthelper

import me.andannn.aozora.core.domain.model.Page

interface AozoraPageLayoutHelper {
    fun Page.layout(): Page.LayoutPage
}
