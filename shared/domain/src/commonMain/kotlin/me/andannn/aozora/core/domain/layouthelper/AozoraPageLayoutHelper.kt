/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.core.domain.layouthelper

import me.andannn.aozora.core.domain.model.Page
import me.andannn.aozora.core.domain.model.PageMetaData

interface AozoraPageLayoutHelper {
    fun Page.layout(): Page.ContentPage

    interface Factory {
        fun create(pageMetaData: PageMetaData): AozoraPageLayoutHelper
    }
}
