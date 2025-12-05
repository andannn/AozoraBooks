/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.viewer.page.rendering.adapters

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.FontStyle
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.ElementRenderAdapterV2
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.MeasureHelper

class ImageRenderAdapterV2(
    private val measureHelper: MeasureHelper,
) : ElementRenderAdapterV2 {
    override fun DrawScope.draw(
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
    ): Size? {
        if (element !is AozoraElement.Illustration) return null
        return Size(0f, 0f)
    }
}
