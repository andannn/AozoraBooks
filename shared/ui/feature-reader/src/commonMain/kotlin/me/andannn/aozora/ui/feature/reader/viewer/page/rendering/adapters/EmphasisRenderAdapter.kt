/*
 * Copyright 2025, the AozoraBooks project contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package me.andannn.aozora.ui.feature.reader.viewer.page.rendering.adapters

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import me.andannn.aozora.core.domain.model.AozoraElement
import me.andannn.aozora.core.domain.model.FontStyle
import me.andannn.aozora.ui.feature.reader.viewer.page.rendering.MeasureHelper

class EmphasisRenderAdapter(
    private val measureHelper: MeasureHelper,
) : BasicTextRenderAdapter<AozoraElement.Emphasis>(measureHelper) {
    override fun DrawScope.draw(
        x: Float,
        y: Float,
        element: AozoraElement.Emphasis,
        fontStyle: FontStyle?,
    ): Size? {
        if (fontStyle == null) {
            error("fontStyle must not be null $element")
        }
// TODO: Draw emphasis
        return drawWithScope(this, x, y, element, fontStyle)
    }
}
