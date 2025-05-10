/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.viewer.page.rendering.adapters

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import me.andannn.aozora.core.data.common.AozoraElement
import me.andannn.aozora.core.data.common.FontStyle
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.MeasureHelper

class EmphasisRenderAdapterV2(
    private val measureHelper: MeasureHelper,
) : BasicTextRenderAdapterV2(measureHelper) {
    override fun DrawScope.draw(
        x: Float,
        y: Float,
        element: AozoraElement,
        fontStyle: FontStyle?,
    ): Size? {
        element as? AozoraElement.Emphasis ?: return null
        if (fontStyle == null) {
            error("fontStyle must not be null $element")
        }

        return drawWithScope(this, x, y, element, fontStyle)
    }
}
